import { BaseHandler } from '../BaseHandler'

export class ArticlePageHandler extends BaseHandler {
  private readonly main = $('main')
  private readonly article = $('article')

  private configureViewColumns() {
    if (innerWidth <= 958) {
      this.main.removeClass('columns')
      this.article.removeClass('column')
    } else {
      this.main.addClass('columns')
      this.article.addClass('column')
    }
  }

  public handle(): void {
    // this.configureViewColumns()
    // window.addEventListener('resize', () => this.configureViewColumns())
  }
}
