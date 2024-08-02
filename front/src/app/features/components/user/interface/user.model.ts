export interface Subject {
  id: number;
  title: string;
  date: Date;
  description: string;
  users?: User[];
  followed: boolean;
}

export interface User {
  id: number;
  userName: string;
  email: string;
  password: string;
  token: string;
  subscription: Subject[];
  subjects: Subject[];
}
