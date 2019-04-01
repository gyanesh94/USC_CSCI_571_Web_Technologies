import { ShippingModel } from './shipping.model';

export interface SearchResultModel {
  index: string;
  image: string | null;
  title: string | null;
  productId: string;
  price: string | null;
  shipping: ShippingModel | null;
  zipcode: string | null;
  sellerName: string | null;
  highlighted: boolean;
  inWishList: boolean;
}
