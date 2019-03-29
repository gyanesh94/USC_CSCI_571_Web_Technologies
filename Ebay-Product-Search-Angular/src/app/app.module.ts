import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatAutocompleteModule, MatInputModule, MatFormFieldModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchComponentComponent } from './search-component/search-component.component';
import { LoggingService } from './services/logging.service';
import { AppConfig } from './app.config';
import { ResultComponent } from './result/result.component';
import { WishListComponent } from './wish-list/wish-list.component';
import { ProductComponent } from './product/product.component';
import { InformationComponent } from './product/information/information.component';
import { PhotosComponent } from './product/photos/photos.component';
import { ShippingComponent } from './product/shipping/shipping.component';
import { SellerComponent } from './product/seller/seller.component';
import { SimilarProductsComponent } from './product/similar-products/similar-products.component';
import { StateService } from './services/state.service';
import { GeoLocationService } from './services/geoLocation.service';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponentComponent,
    ResultComponent,
    WishListComponent,
    ProductComponent,
    InformationComponent,
    PhotosComponent,
    ShippingComponent,
    SellerComponent,
    SimilarProductsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatInputModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
  ],
  providers: [LoggingService, AppConfig, StateService, GeoLocationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
