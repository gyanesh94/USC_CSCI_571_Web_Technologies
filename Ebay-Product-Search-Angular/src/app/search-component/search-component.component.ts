import { Component, OnInit, ViewChild } from '@angular/core';
import { NgModel, NgForm } from '@angular/forms';

import { LoggingService } from 'src/services/logging.service';

@Component({
  selector: 'app-search-component',
  templateUrl: './search-component.component.html',
  styleUrls: ['./search-component.component.css']
})
export class SearchComponentComponent implements OnInit {
  @ViewChild('zipCodeForm') zipCodeForm: NgModel;
  @ViewChild('searchForm') searchForm: NgForm;

  search = {
    keyword: '',
    category: 'all',
    condition: {
      new: false,
      used: false,
      unspecified: false
    },
    shipping: {
      localPickupOnly: false,
      freeShipping: false
    },
    distance: '',
    fromHere: true,
    zipCode: ''
  };

  isSubmitValid = false;

  constructor(private loggingService: LoggingService) {
  }

  ngOnInit() {
    this.searchForm.valueChanges.subscribe(
      form => {
        this.checkSubmitValid();
      }
    );
  }

  locationMethodChange(value: string) {
    if (value === 'here') {
      this.zipCodeForm.reset();
    }
  }

  checkSubmitValid() {
    if (!this.searchForm.valid) {
      this.isSubmitValid = false;
      return;
    }
    const regexp = /^\d{5}$/;
    if (!this.search.fromHere && !regexp.test(this.zipCodeForm.value)) {
      this.isSubmitValid = false;
      return;
    }
    this.isSubmitValid = true;

  }

  onSubmit() {
  }

}
