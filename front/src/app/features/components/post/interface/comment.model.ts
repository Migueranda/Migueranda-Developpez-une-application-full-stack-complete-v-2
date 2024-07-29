
export interface Comment {
    id?: number;
    description: string;
    date?: Date;
    userId: number;
    userName: string;
  }

  export interface CreateComment {
    postId: number;
    userId: number;
    description: string;
    date:  Date;
  }
  