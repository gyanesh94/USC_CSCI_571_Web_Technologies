export class SearchData {
  category: string;
  condition = {
    conditionNew: false,
    conditionUsed: false,
    conditionUnspecified: false
  };
  distance: string | null;
  here: string;
  keyword: string;
  shipping = {
    localPickupOnly: false,
    freeShipping: false
  };
  zipCode: string;
}
