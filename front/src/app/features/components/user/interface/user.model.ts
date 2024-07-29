
export interface Subject {
  id: number;
  title: string;
  date: Date;
  description: string;
  users?: User[];
}

export interface User {
  subscription: Subject[];
  id: number;
  userName: string;
  email: string;
  password: string;
  token: string;
  subjects?: Subject[];
}
