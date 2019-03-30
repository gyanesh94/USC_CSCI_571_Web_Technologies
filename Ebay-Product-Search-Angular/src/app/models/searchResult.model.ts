export class SearchResultModel {
  index: string;
  image: string | null;
  title: string | null;
  productId: string;
  price: string | null;
  shippingPrice: string | null;
  zipcode: string | null;
  sellerName: string | null;
  highlighted: boolean;
  inWishList: boolean;
}
