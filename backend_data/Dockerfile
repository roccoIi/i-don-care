FROM python:3.12-slim

RUN apt-get update && \
    apt-get install -y gcc pkg-config default-libmysqlclient-dev

WORKDIR /app

COPY requirements.txt /app/

RUN pip install --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

COPY main.py /app

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8089", "--reload"]