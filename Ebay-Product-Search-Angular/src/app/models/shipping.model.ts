export interface ShippingModel {
  cost: string | null;
  locations: string | null;
  handlingTime: string | null;
  expedited: boolean | null;
  oneDay: boolean | null;
  returnAccepted: boolean | null;
}
