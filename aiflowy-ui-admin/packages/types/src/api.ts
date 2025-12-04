export interface RequestResult<T = any> {
  data: T;
  errorCode: number;
  message: string;
}
