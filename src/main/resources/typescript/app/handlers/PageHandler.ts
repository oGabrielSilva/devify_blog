import { AdminNewUserPageHandler } from './admin/AdminNewUserPageHandler'
import { AdminUsersPageHandler } from './admin/AdminUsersPageHandler'
import { EditArticleMetadataPageHandler } from './article/EditArticleMetadataPageHandler'
import { EditArticlePageHandler } from './article/EditArticlePageHandler'
import { ModArticlesPageHandler } from './article/ModArticlesPageHandler'
import { NewArticlePageHandler } from './article/NewArticlePageHandler'
import { InternalProfilePageHandler } from './profile/InternalProfilePageHandler'
import { SessionPageHandler } from './session/SessionPageHandler'
import { StackListPageHandler } from './stack/StackListPageHandler'
import { StackLockedPageHandler } from './stack/StackLockedPageHandler'

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
      case 'stack_list':
        StackListPageHandler.fast.handle()
        break
      case 'mod_stacks_locked':
        StackLockedPageHandler.fast.handle()
        break
      case 'write_article':
        NewArticlePageHandler.fast.handle()
        break
      case 'edit_article':
        EditArticlePageHandler.fast.handle()
        break
      case 'edit_article_metadata':
        EditArticleMetadataPageHandler.fast.handle()
        break
      case 'mod_articles':
        ModArticlesPageHandler.fast.handle()
        break
      case 'admin_users':
        AdminUsersPageHandler.fast.handle()
        break
      case 'admin_user':
        AdminNewUserPageHandler.fast.handle()
        break
    }
  }

  public static get fast() {
    return new PageHandler()
  }
}
