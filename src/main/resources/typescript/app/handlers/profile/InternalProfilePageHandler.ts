import { BaseHandler } from '../BaseHandler'
import { configureAvatar } from './profile/avatar'
import { configureProfileForm } from './profile/form'
import { configureName } from './profile/name'
import { configurePseudonym } from './profile/pseudonym'
import { configureSecurityPartEmail } from './security/email'
import { configureChangePasswordPart } from './security/password'
import { configureSecurityUsernamePart } from './security/username'

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

  public readonly social = {
    conatiner: $('#social-container'),
  }

  public readonly security = {
    conatiner: $('#security-container'),
    email: {
      form: $<HTMLFormElement>('#email-form'),
      lab: $('#email-lab'),
      modal: $('#email-modal'),
      input: $<HTMLInputElement>('#email'),
      isValid: false,
      password: {
        input: $('#password'),
        isValid: false,
        helper: $('#password-helper'),
      },
    },
    username: {
      form: $<HTMLFormElement>('#username-form'),
      lab: $('#inp-username-readonly'),
      modal: $('#username-modal'),
      input: document.querySelector('#inp-username') as HTMLInputElement,
      isValid: false,
      len: $('#username-len'),
    },
    password: {
      button: $('#change-pass'),
      modal: $('#password-modal'),
      form: $<HTMLFormElement>('#password-form'),
      inputs: {
        current: {
          input: $('#current-password'),
          isValid: false,
          helper: $('#current-password-helper'),
        },
        new: {
          input: $('#new-password'),
          isValid: false,
          helper: $('#new-password-helper'),
        },
        confirm: {
          input: $('#confirm-password'),
          isValid: false,
        },
      },
    },
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
        this.social.conatiner.addClass('is-hidden')
        this.security.conatiner.addClass('is-hidden')

        $(tab).addClass('is-active')
        if (tab.dataset.tab === 'profile') {
          this.profile.form.removeClass('is-hidden')
          history.pushState(null, '', location.pathname + `?tab=profile`)
          return
        }
        if (tab.dataset.tab === 'social') {
          this.social.conatiner.removeClass('is-hidden')
          history.pushState(null, '', location.pathname + `?tab=social`)
          return
        }
        if (tab.dataset.tab === 'security') {
          history.pushState(null, '', location.pathname + `?tab=security`)
          this.security.conatiner.removeClass('is-hidden')
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

  private configureSecurity() {
    this.security.email.modal.find('button[type="reset"]').on('click', () => {
      this.security.email.modal.find('input').removeClass('is-danger')
      this.security.email.password.helper.addClass('is-hidden')
    })

    configureSecurityUsernamePart(this)
    configureSecurityPartEmail(this)
    configureChangePasswordPart(this)
  }

  public handle(): void {
    this.configureTabs()

    this.configureProfile()
    this.configureSecurity()
  }
}
