import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatAutocompleteModule, MatInputModule, MatFormFieldModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { FlexLayoutModule } from '@angular/flex-layout';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchComponent } from './search/search.component';
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
import { TruncatePipe } from './pipes/truncate.pipe';
import { ProductService } from './services/product.service';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    ResultComponent,
    WishListComponent,
    ProductComponent,
    InformationComponent,
    PhotosComponent,
    ShippingComponent,
    SellerComponent,
    SimilarProductsComponent,
    TruncatePipe
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
    PaginationModule.forRoot(),
    TooltipModule.forRoot(),
    FlexLayoutModule,
    ModalModule.forRoot()
  ],
  providers: [
    LoggingService,
    AppConfig,
    StateService,
    GeoLocationService,
    ProductService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
