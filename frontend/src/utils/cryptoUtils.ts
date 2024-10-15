import JSEncrypt from 'jsencrypt';

// 공개 키 설정 (환경 변수에서 불러오기)
const PUBLIC_KEY = import.meta.env.VITE_PUBLIC_KEY;

// RSA로 데이터를 암호화하는 함수

export const encryptDataWithRSA = async(data: string) => {
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(PUBLIC_KEY);
  return encrypt.encrypt(data); // 데이터를 RSA로 암호화
};

// AES 키 생성 (256-bit AES 키 생성)
export const generateAESKey = () => {
  const aesKey = window.crypto.getRandomValues(new Uint8Array(32)); // 256-bit
  return aesKey;
};

// AES로 데이터 암호화 (SubtleCrypto API 사용)
export const encryptDataWithAES = async (aesKey: Uint8Array, data: any) => {
  const iv = window.crypto.getRandomValues(new Uint8Array(12)); // 96-bit IV
  const encodedData = new TextEncoder().encode(JSON.stringify(data));
  
  const encryptedData = await window.crypto.subtle.encrypt(
    {
      name: "AES-GCM",
      iv: iv
    },
    await importAESKey(aesKey),
    encodedData
  );
  
  return { iv: Buffer.from(iv).toString('base64'), encryptedData: Buffer.from(encryptedData).toString('base64') };
};

// AES로 데이터 복호화
export const decryptDataWithAES = async (aesKey: Uint8Array, encryptedData: string, iv: string) => {
  const ivBytes = Buffer.from(iv, 'base64');
  const encryptedBytes = Buffer.from(encryptedData, 'base64');
  
  const decryptedData = await window.crypto.subtle.decrypt(
    {
      name: "AES-GCM",
      iv: ivBytes
    },
    await importAESKey(aesKey),
    encryptedBytes
  );
  
  return JSON.parse(new TextDecoder().decode(decryptedData));
};

// AES 키 가져오기 (SubtleCrypto에서 사용하기 위해)
const importAESKey = async (key: Uint8Array) => {
  return window.crypto.subtle.importKey(
    "raw",
    key,
    { name: "AES-GCM" },
    false,
    ["encrypt", "decrypt"]
  );
};
