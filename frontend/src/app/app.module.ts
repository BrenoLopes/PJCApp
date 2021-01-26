import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { CoreModule } from '@core/core.module';
import { PagesModule } from '@pages/pages.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CoreModule,
    PagesModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
