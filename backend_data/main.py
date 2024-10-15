from fastapi import FastAPI, APIRouter, HTTPException, Depends, Body
from pydantic import BaseModel
import httpx
from typing import List, Optional,ClassVar
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import create_engine, Column, BigInteger, String, DateTime, Boolean, ForeignKey, func
from datetime import datetime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

# Bank
BANK_DATABASE_URL = "mysql+pymysql://root:ssafy@j11a603.p.ssafy.io:3308/bank"
bank_engine = create_engine(BANK_DATABASE_URL)
BankSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=bank_engine)

def get_bank_db():
    db = BankSessionLocal()
    try:
        yield db
    finally:
        db.close()

# User
USER_DATABASE_URL = "mysql+pymysql://root:ssafy@j11a603.p.ssafy.io:3306/idoncare"
user_engine = create_engine(USER_DATABASE_URL)
UserSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=user_engine)

def get_user_db():
    db = UserSessionLocal()
    try:
        yield db
    finally:
        db.close()

# Quest
QUEST_DATABASE_URL = "mysql+pymysql://root:ssafy@j11a603.p.ssafy.io:3307/quest"
quest_engine = create_engine(QUEST_DATABASE_URL)
QuestSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=quest_engine)

def get_quest_db():
    db = QuestSessionLocal()
    try:
        yield db
    finally:
        db.close()


Base = declarative_base()

class Accounts(Base):
    __tablename__ = "account"

    account_id = Column(BigInteger, primary_key=True)
    account_num = Column(String(255))
    balance = Column(BigInteger)
    user_id = Column(BigInteger)

class AccountsSchema(BaseModel):
    account_id: int 
    account_num: str
    balance: int 
    user_id: int

    class Config:
        # orm_mode = True
        from_attributes=True

class CardTrans(Base):
    __tablename__ = "card_trans"

    card_trans_id = Column(BigInteger, primary_key=True, autoincrement=True)
    create_at = Column(DateTime, default=datetime.utcnow)
    merchant_id = Column(BigInteger)
    merchant_name = Column(String(255))
    transaction_balance = Column(BigInteger)
    transaction_date = Column(String(255))
    transaction_time = Column(String(255))
    transaction_unique_no = Column(BigInteger)
    account_id = Column(BigInteger)

class CardTransSchema(BaseModel):
    card_trans_id: int  
    create_at: datetime
    merchant_id: int
    merchant_name: str
    transaction_balance: int
    transaction_date: str
    transaction_time: str
    transaction_unique_no: int
    account_id: int

    class Config:
        orm_mode = True
        # from_attributes=True

class Missions(Base):
    __tablename__ = "mission"

    mission_id = Column(BigInteger, primary_key=True, autoincrement=True)
    content = Column(String(255))
    relation_id = Column(BigInteger)
    state = Column(String(255))
    title = Column(String(255))

class Relation(Base):
    __tablename__ = "relation"

    relation_id = Column(BigInteger, primary_key=True, autoincrement=True)
    child_id = Column(BigInteger)
    parent_id = Column(BigInteger)

class MissionsSchema(BaseModel):
    mission_id: int  
    content: str
    relation_id: int
    state: str
    title: str

    class Config:
        orm_mode = True
    
class Quiz(Base):
    __tablename__ = "quiz_solved"

    solved_id = Column(BigInteger, primary_key=True, autoincrement=True)
    is_solved = Column(Boolean)
    quiz_rating = Column(BigInteger)
    relation_id = Column(BigInteger)

class CoinBox(Base):
    __tablename__ = "coinbox"

    coinbox_id = Column(BigInteger, primary_key=True, autoincrement=True)
    goal_amount= Column(BigInteger)
    goal_title = Column(String(255))
    relation_id = Column(BigInteger)

class CoinBoxSchema(BaseModel):
    coinbox_id: int  
    goal_amount: int
    goal_title: str
    relation_id: int

    class Config:
        orm_mode = True


app = FastAPI()

@app.get("/data/accounts", response_model=List[AccountsSchema])
def read_accounts(db: Session = Depends(get_bank_db)):
    accounts = db.query(Accounts).all()
    return accounts

class CardTransRequest(BaseModel):
    account_num: str

class RelationRequest(BaseModel):
    relation_id: int

@app.post("/data/saving_rate", response_model=int)
def read_missions(request: RelationRequest, quest_db: Session = Depends(get_quest_db), bank_db: Session = Depends(get_bank_db), user_db: Session = Depends(get_user_db)):
    count = quest_db.query(Missions).filter(
        Missions.relation_id == request.relation_id,
        Missions.state == 'COMPLETED'
    ).count()

    rate = 0

    if count == 2:
        rate += 10
    elif count == 3:
        rate += 20
    elif count >= 4:
        rate += 30
    
    quiz = quest_db.query(Quiz).filter(Quiz.relation_id == request.relation_id).first()

    if quiz:
        if quiz.is_solved:
            rate += 15
            if quiz.quiz_rating > 0:
                rate += 15
    
    savings_count = quest_db.query(CoinBox).filter(CoinBox.relation_id == request.relation_id).count()

    if savings_count > 5:
        rate += 20
    elif savings_count:
        rate += 10
    
    related_user = user_db.query(Relation).filter(Relation.relation_id == request.relation_id).first()
    account = bank_db.query(Accounts).filter(Accounts.user_id == related_user.child_id).first()
    if account is None:
        raise HTTPException(status_code=404, detail={"error": "존재하지 않는 계좌번호"})
    card_trans = bank_db.query(CardTrans).filter(CardTrans.account_id == account.account_id).all()
    if card_trans is None:
        raise HTTPException(status_code=404, detail={"error": "카드가 등록되지 않은 사용자"})
    total_balance = sum(t.transaction_balance for t in card_trans)

    if 300000 <= total_balance < 350000:
        rate += 5
    elif 250000 <= total_balance < 300000:
        rate + 10
    elif total_balance < 250000:
        rate + 20
    
    return rate

@app.post("/data/card_trans", response_model=List[CardTransSchema])
def read_card_trans(request: CardTransRequest, db: Session = Depends(get_bank_db)):
    account = db.query(Accounts).filter(Accounts.account_num == request.account_num).first()
    card_trans = db.query(CardTrans).filter(CardTrans.account_id == account.account_id).all()
    if account is None:
        raise HTTPException(status_code=404, detail={"error": "존재하지 않는 계좌번호"})
    return card_trans

@app.post("/data/sum_card", response_model=dict)
def sum_card_trans(request: RelationRequest, bank_db: Session = Depends(get_bank_db), user_db: Session = Depends(get_user_db)):
    related_user = user_db.query(Relation).filter(Relation.relation_id == request.relation_id).first()
    account = bank_db.query(Accounts).filter(Accounts.user_id == related_user.child_id).first()
    if account is None:
        raise HTTPException(status_code=404, detail={"error": "존재하지 않는 계좌번호"})
    card_trans = bank_db.query(CardTrans).filter(CardTrans.account_id == account.account_id).all()
    if card_trans is None:
        raise HTTPException(status_code=404, detail={"error": "카드가 등록되지 않은 사용자"})
    total_balance = sum(t.transaction_balance for t in card_trans)
    return {
        "total_balance": total_balance
    }

@app.post("/data/similarity", response_model=int)
def read_similarity(request: RelationRequest, bank_db: Session = Depends(get_bank_db), user_db: Session = Depends(get_user_db)):
    related_user = user_db.query(Relation).filter(Relation.relation_id == request.relation_id).first()
    account = bank_db.query(Accounts).filter(Accounts.user_id == related_user.child_id).first()
    if account is None:
        raise HTTPException(status_code=404, detail={"error": "존재하지 않는 계좌번호"})
    
    account_id = account.account_id
    target_card_trans = bank_db.query(CardTrans).filter(CardTrans.account_id == account_id).all()
    if not target_card_trans:
        raise HTTPException(status_code=404, detail={"error": "카드가 등록되지 않은 사용자"})
    
    all_card_trans = bank_db.query(CardTrans).all()
    similarities = {}
    target_vectors = np.array([[trans.merchant_id, trans.transaction_balance] for trans in target_card_trans])
    
    for trans in all_card_trans:
        if trans.account_id != account_id:
            other_card_trans = bank_db.query(CardTrans).filter(CardTrans.account_id == trans.account_id).all()
            if not other_card_trans:
                continue
            
            other_vectors = np.array([[ot.merchant_id, ot.transaction_balance] for ot in other_card_trans])
            
            if target_vectors.shape[0] > 0 and other_vectors.shape[0] > 0:
                target_avg_vector = np.mean(target_vectors, axis=0).reshape(1, -1)
                other_avg_vector = np.mean(other_vectors, axis=0).reshape(1, -1)
                sim = cosine_similarity(target_avg_vector, other_avg_vector)[0][0]
                similarities[trans.account_id] = sim
    
    if not similarities:
        raise HTTPException(status_code=404, detail={"error": "유사 계좌 존재하지 않음"})
    
    most_similar_account_id = max(similarities, key=similarities.get)
    max_account = bank_db.query(Accounts).filter(Accounts.account_id == most_similar_account_id).first()
    
    if max_account is None:
        raise HTTPException(status_code=404, detail={"error": "유사 계좌 존재하지 않음"})
    
    max_user = user_db.query(Relation).filter(Relation.child_id == max_account.user_id).first()

    
    return max_user.relation_id

@app.post("/data/category_sum", response_model=list)
def sum_card_trans_by_category(request: RelationRequest, bank_db: Session = Depends(get_bank_db), user_db: Session = Depends(get_user_db)):
    related_user = user_db.query(Relation).filter(Relation.relation_id == request.relation_id).first()
    account = bank_db.query(Accounts).filter(Accounts.user_id == related_user.child_id).first()
    if account is None:
        raise HTTPException(status_code=404, detail={"error": "Account not found"})
    
    account_id = account.account_id

    categories = {
        "카페": [2138, 2139],
        "도서/문구": [2140, 2141],
        "식사": [2142, 2143],
        "간식": [2144, 2145],
        "게임": [2146, 2147],
        "쇼핑": [2148, 2149]
    }

    category_sums = []
    target_card_trans = bank_db.query(CardTrans).filter(CardTrans.account_id == account_id).all()

    for category, merchant_ids in categories.items():
        total_sum = sum(trans.transaction_balance for trans in target_card_trans if trans.merchant_id in merchant_ids)
        category_sums.append({
            "subject": category,
            "A": total_sum
        })
    
    max_total_sum = max(category["A"] for category in category_sums) if category_sums else 0

    for category in category_sums:
        category["fullMark"] = max_total_sum

    return category_sums

# @app.post("/data/savings_rate", response_model=int)











# CORS 설정 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 모든 도메인에서의 요청 허용
    allow_credentials=True,
    allow_methods=["*"],  # 모든 HTTP 메서드 허용
    allow_headers=["*"],  # 모든 헤더 허용
)


@app.get("/data/test")
async def read_root():
    return {"Hello": "World"}

class Header(BaseModel):
    apiName: str
    transmissionDate: str
    transmissionTime: str
    institutionCode: str
    fintechAppNo: str
    apiServiceCode: str
    institutionTransactionUniqueNo: str
    apiKey: str
    userKey: str

class TransactionRequest(BaseModel):
    Header: Header
    cardNo: str
    cvc: str
    startDate: str
    endDate: str

class TransactionList(BaseModel):
    transactionUniqueNo: str
    merchantId: str
    merchantName: str
    transactionDate: str
    transactionTime: str
    transactionBalance: str

class REC(BaseModel):
    cardName: str
    cardNo: str
    estimatedBalance: str
    transactionList: List[TransactionList]

class TransResponse(BaseModel):
    REC: REC



@app.post("/data/trans-history")
async def trans_history(request_data: TransactionRequest):
    external_api_url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/creditCard/inquireCreditCardTransactionList"

    async with httpx.AsyncClient() as client:
        response = await client.post(external_api_url, json=request_data.dict())

    response_data = TransResponse.parse_obj(response.json())

    return response_data.REC.transactionList