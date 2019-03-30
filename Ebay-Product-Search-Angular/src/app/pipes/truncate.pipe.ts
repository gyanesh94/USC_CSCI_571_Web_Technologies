import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  transform(value: string) {
    if (value.length > 35) {
      const lastIndex = value.substr(0, 35).lastIndexOf(' ');
      return `${value.substr(0, lastIndex)}...`;
    } else {
      return `${value}`;
    }
  }
}
