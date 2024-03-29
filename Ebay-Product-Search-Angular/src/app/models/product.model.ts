import { SellerModel } from './seller.model';

export interface ProductModel {
  title: string | null;
  productId: string | null;
  productUrl: string | null;
  productImages: string[];
  subtitle: string | null;
  price: number | null;
  location: string | null;
  returnPolicy: string | null;
  itemSpecifics: {
    name: string,
    value: string
  }[];
  seller: SellerModel;
}
