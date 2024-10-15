import { axiosCommonInstance } from "./axiosInstance";

interface ApiResponse<T> {
  status: number
  message: string
  code: string
  data: T
}

interface ReadQuizRequest {
  relationId: number;
}

interface ReviewQuizRequest {
  relationId: number,
  realAnswer: string,
  userAnswer: string
}

export const fetchReadQuiz = async (readQuizRequest:ReadQuizRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('quest/quiz/question', readQuizRequest)
  return data;
}

export const fetchReviewQuiz = async (reviewQuizRequest:ReviewQuizRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('quest/quiz/reviewQuiz', reviewQuizRequest)
  return data;
}