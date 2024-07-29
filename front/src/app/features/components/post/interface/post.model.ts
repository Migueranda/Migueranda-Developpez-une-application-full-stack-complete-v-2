

export interface Post {
    id: number;
    title: string;
    description: string;
    date:Date;
    themeId: number; 
    userId: number; 
    createdAt?: string;
    updatedAt?: string;
  }  
 
  export interface User {
    id: number;
    userName?: string;
   
  }
  
  export interface Subject {
    id: number;
    title: string;
    
  }
  