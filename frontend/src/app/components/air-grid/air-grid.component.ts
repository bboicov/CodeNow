import { AfterViewInit, Component, NgZone, OnInit, ViewChild } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MatSort } from '@angular/material/sort';
import { TableVirtualScrollDataSource } from 'ng-table-virtual-scroll';
import { HttpClient } from '@angular/common/http';
import { concat, merge, Observable, of as observableOf, Subject } from 'rxjs/index';
import { catchError, filter, map, pairwise, startWith, switchMap, throttleTime } from 'rxjs/internal/operators';
import { CdkVirtualScrollViewport } from '@angular/cdk/scrolling';


@Component({
  selector: 'app-air-grid',
  templateUrl: './air-grid.component.html',
  styleUrls: ['./air-grid.component.css']
})
export class AirGridComponent implements OnInit, AfterViewInit {

  @ViewChild('scroller') scroller: CdkVirtualScrollViewport;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  data = new TableVirtualScrollDataSource();

  page = 0;

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;
  displayedColumns: string[] = ['Country', 'City', 'Location', 'Parameter', 'Value', 'Unit', 'Updated'];
  dataSource: ExampleHttpDatabase | null;

  subject = new Subject<any>();

  constructor(private _httpClient: HttpClient, private ngZone: NgZone) {
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {

    this.scroller.elementScrolled().pipe(
      map(() => this.scroller.measureScrollOffset('bottom')),
      pairwise(),
      filter(([y1, y2]) => (y2 < y1 && y2 < 140)),
      throttleTime(200)
    ).subscribe(() => {
        this.ngZone.run(() => {
          this.isLoadingResults = true;
          const result: any = this.dataSource!.getNextPage(this.resultsLength).subscribe(data => {
            this.isLoadingResults = false;
            this.data = new TableVirtualScrollDataSource([...this.data.data, ...data.content]);
          });
        });
      }
    );

    this.dataSource = new ExampleHttpDatabase(this._httpClient);

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => {
        this.page = 0
    });

    merge(this.sort.sortChange)
      .pipe(
        startWith({}),
        switchMap(() => {
          console.log(this.sort)
          this.isLoadingResults = true;
          return this.dataSource!.getRepoIssues(
            this.sort.active,
            this.sort.direction,
            0
          );
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.isRateLimitReached = false;
          this.resultsLength = data.totalElements;

          return data.content;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          // Catch if the AirQuality API has reached its rate limit. Return empty data.
          this.isRateLimitReached = true;
          return observableOf([]);
        })
      )
      .subscribe(data => (this.data = new TableVirtualScrollDataSource(data)));
  }

}

export class ExampleHttpDatabase {
  sort;
  order;
  page;

  constructor(private _httpClient: HttpClient) {
  }

  getRepoIssues(sort: string,
                order: string,
                page: number): Observable<any> {
    this.sort = sort;
    this.order = order;
    this.page = page;
    const href = 'http://localhost:8080/api/results/pageable';
    const requestUrl = `${href}?sort=${sort},${order}&page=${page + 1}`;

    return this._httpClient.get<any>(requestUrl)
  }

  getNextPage(totalPages) {
    this.page++;
    if(totalPages >= this.page) {
      return this.getRepoIssues(this.sort, this.order, this.page)
    } else {
      return observableOf({content: []})
    }
  }
}