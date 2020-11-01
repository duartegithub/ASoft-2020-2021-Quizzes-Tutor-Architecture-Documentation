import { ISOtoString } from '@/services/ConvertDateService';

export default class Reply {
  id!: number;
  username!: string;
  userId!: number;
  date!: string | null;
  message!: string;
  public!: boolean;

  constructor(jsonObj?: Reply) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.username = jsonObj.username;
      this.userId = jsonObj.userId;
      this.date = ISOtoString(jsonObj.date);
      this.message = jsonObj.message;
      this.public = jsonObj.public;
    }
  }
}
