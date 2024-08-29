import { BaseHandler } from '../BaseHandler'
import { configureAvatar } from './profile/avatar'
import { configureProfileForm } from './profile/form'
import { configureName } from './profile/name'
import { configurePseudonym } from './profile/pseudonym'

export class InternalProfilePageHandler extends BaseHandler {
  private readonly tabs = $('#tabs').find('li')
  public readonly profile = {
    form: $('#internal-profile-form'),
    name: {
      input: document.querySelector('#inp-name') as HTMLInputElement,
      isValid: false,
      helper: $('#name-helper'),
      len: $('#name-len'),
    },
    pseudonym: {
      input: document.querySelector('#inp-pseud') as HTMLInputElement,
      len: $('#pseudonym-len'),
    },
    avatar: {
      input: $('input[type="file"]').get(0) as HTMLInputElement,
      img: $('.avatar'),
      blob: null as Blob | null,
    },
  }

  public readonly links = {
    form: $('#internal-profile-links-form'),
  }

  private configureTabs() {
    addEventListener('popstate', () => {
      const tab = this.tabs.find(`[data-tab=${location.search.split('tab=')[1]}]`)
      if (tab.length > 0) {
        tab.get(0)?.click()
      } else {
        this.tabs.get(0)?.click()
      }
    })

    this.tabs.each((_, tab) => {
      $(tab).on('click', () => {
        this.tabs.each((_i, t) => {
          $(t).removeClass('is-active')
        })

        this.profile.form.addClass('is-hidden')
        this.links.form.addClass('is-hidden')
        $(tab).addClass('is-active')

        if (tab.dataset.tab === 'profile') {
          this.profile.form.removeClass('is-hidden')
          history.pushState(null, '', location.pathname + `?tab=profile`)
          return
        }
        if (tab.dataset.tab === 'links') {
          this.links.form.removeClass('is-hidden')
          history.pushState(null, '', location.pathname + `?tab=links`)
          return
        }
        if (tab.dataset.tab === 'security') {
          history.pushState(null, '', location.pathname + `?tab=security`)
          return
        }
      })
    })
  }

  private configureProfile() {
    configureAvatar(this)
    configureName(this)
    configurePseudonym(this)

    configureProfileForm(this)
  }

  public handle(): void {
    this.configureTabs()

    this.configureProfile()
  }
}
