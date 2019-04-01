export interface SearchFormModel {
  category: string;
  condition: {
    New: boolean,
    Used: boolean,
    Unspecified: boolean
  };
  distance: string | null;
  here: string;
  keyword: string;
  shipping: {
    localPickupOnly: boolean,
    freeShipping: boolean
  };
  zipcode: string;
}

export const emptySearchFormModel = (): SearchFormModel => ({
  category: 'all',
  condition: {
    New: false,
    Used: false,
    Unspecified: false
  },
  distance: null,
  here: 'here',
  keyword: '',
  shipping: {
    localPickupOnly: false,
    freeShipping: false
  },
  zipcode: '',
});
