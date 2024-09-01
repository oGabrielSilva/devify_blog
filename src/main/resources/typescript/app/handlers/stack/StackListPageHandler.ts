import { openModal } from '../../lib/BulmaCss'
import { BaseHandler } from '../BaseHandler'
import { StackCreateForm } from './create/StackCreateForm'

export class StackListPageHandler extends BaseHandler {
  private readonly buttonOpenNewStackModal = $('#open-stack-modal')
  private readonly stackForm = new StackCreateForm()
  private readonly table = document.querySelector('table#stack-list') as HTMLTableElement

  public handle(): void {
    this.buttonOpenNewStackModal.on('click', () => {
      this.stackForm.openFormModal()
    })

    this.stackForm.run()

    $(this.table)
      .find('.show-desc')
      .on('click', (e) => {
        if (e.currentTarget.parentElement) {
          openModal($(e.currentTarget.parentElement).find('.modal')[0])
        }
      })

    $(this.table)
      .find('.meta-description-empty')
      .on('click', (e) => {
        $(this.table)
          .find('tr')
          .each((_, tr) => {
            if (tr.contains(e.currentTarget)) {
              tr.querySelector<HTMLAnchorElement>('button[data-edit]')?.click()
              setTimeout(
                () => anim.shakeX(tr.querySelector<HTMLElement>('textarea[name="metaDescription"]')!),
                1000
              )
            }
          })
      })

    $(this.table)
      .find('button[data-edit]')
      .each((_, button) => {
        $(button).on('click', () => {
          const modal = button.parentElement?.querySelector<HTMLElement>('.modal[data-edit]')
          if (modal) openModal(modal)
        })
      })

    $(this.table)
      .find('button[data-disable]')
      .each((_, button) => {
        $(button).on('click', () => {
          const modal = button.parentElement?.querySelector<HTMLElement>('.modal[data-disable]')
          if (modal) openModal(modal)
        })
      })

    $<HTMLFormElement>('.stack-form-edit').each((_, form) => {
      new StackCreateForm(form).run()
    })

    console.log(this)
  }
}
