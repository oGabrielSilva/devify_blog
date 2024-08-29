import { InternalProfilePageHandler } from './profile/InternalProfilePageHandler'
import { SessionPageHandler } from './session/SessionPageHandler'

export class PageHandler {
  private readonly meta = document.head.querySelector<HTMLMetaElement>('meta[data-details="page"]')

  public handle() {
    if (!this.meta) return console.error('Meta handler not found (404)')
    const page = this.meta.dataset.content

    if (!page) return console.error('Meta handler: dataset content not defined (400)')

    switch (page.toLocaleLowerCase()) {
      case 'session':
        SessionPageHandler.fast.handle()
        break
      case 'profile':
        InternalProfilePageHandler.fast.handle()
        break
    }
  }

  public static get fast() {
    return new PageHandler()
  }
}
