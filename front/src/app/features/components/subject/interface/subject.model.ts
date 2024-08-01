export interface Subject {
  id: number;
  title: string;
  date: Date;
  description: string;
  users?: User[];
}

export interface User {
  token:string;
  id: number;
  userName: string;
  // username: string;
  email: string;
  password: string;
  subjects?: Subject[];
}
