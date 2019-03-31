export class SearchFormModel {
  category: string;
  condition = {
    New: false,
    Used: false,
    Unspecified: false
  };
  distance: string | null;
  here: string;
  keyword: string;
  shipping = {
    localPickupOnly: false,
    freeShipping: false
  };
  zipcode: string;
}
