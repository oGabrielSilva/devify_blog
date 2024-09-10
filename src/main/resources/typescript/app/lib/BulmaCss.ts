export class BulmaCss {
  private readonly storageKey = 'key__AppScheme'
  private readonly buttons = $('[data-design]')

  public recoveryDefinedColorScheme(): AppScheme {
    let defined = (document.documentElement.dataset.theme as AppScheme) ?? ''
    if (defined !== 'light' && defined !== 'dark') {
      defined = localStorage.getItem(this.storageKey) as AppScheme
    }
    if (['dark', 'light'].indexOf(defined) > -1) return defined as AppScheme
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
  }

  public updateColorScheme(nextScheme?: AppScheme) {
    const scheme = nextScheme ? nextScheme : this.nextColorScheme()
    this.buttons.get().forEach((button) => {
      const icons = $(button).find('span[data-scheme]')!

      icons.each((_, icon) => {
        if (scheme === icon.dataset.scheme) {
          $(icon).removeClass('is-hidden')
          return
        }
        $(icon).addClass('is-hidden')
      })

      localStorage.setItem(this.storageKey, scheme ? scheme : this.nextColorScheme())
    })
    document.documentElement.dataset.theme = scheme
  }

  public nextColorScheme(): AppScheme {
    return this.recoveryDefinedColorScheme() === 'dark' ? 'light' : 'dark'
  }

  public listenAllThemeButton() {
    this.buttons.get().forEach((button) => {
      $(button).on('click', () => this.updateColorScheme(this.nextColorScheme()))
    })
  }

  public animateNavMenu() {
    const navBurger = $('#navbar-burger-button')

    navBurger.on('click', () => {
      const target = $('#' + navBurger.attr('data-target'))
      const isTargetActive = target.hasClass('is-active')

      if (!isTargetActive) {
        target.slideDown()
        navBurger.addClass('is-active')
        target.addClass('is-active')
      } else {
        navBurger.removeClass('is-active')
        target.slideUp({
          complete() {
            target.removeClass('is-active')
          },
        })
      }
    })

    $(window).on('resize', () => {
      $('#' + navBurger.attr('data-target')).css({ display: '' })
    })
  }

  public listenAllGoBackModalButton() {
    $('.modal').each((_, modal) => {
      $(modal)
        .find('.goBack')
        .on('click', () => {
          closeModal(modal)
        })
    })

    globalThis.addEventListener('keydown', (e) => {
      if (e.key && typeof e.key === 'string' && e.key.toLocaleLowerCase() === 'escape') {
        closeAllModals()
      }
    })
  }
}

export function openModal(modal: HTMLElement | string) {
  const $el =
    typeof modal === 'string'
      ? document.querySelector<HTMLElement>(modal.startsWith('#') ? modal : '#' + modal)
      : modal
  if ($el) {
    const animations = [
      'animate__backInDown',
      'animate__backInLeft',
      'animate__backInRight',
      'animate__backInUp',
      'animate__fadeInDownBig',
      'animate__fadeInRightBig',
      'animate__fadeInLeftBig',
    ]
    const card = $el.querySelector<HTMLElement>('.modal-card')!
    card.style.display = 'none'
    anim.otherAnimationByName($el, 'animate__fadeIn', true, 100).addEventOnCompletion(() => {
      card.style.display = ''
      anim.otherAnimationByName(card, animations.pickRandom()!)
      setTimeout(() => $el.classList.add('is-active'), 5)
    })
  }
}

export function closeModal(modal: HTMLElement | string, onComplete?: () => void) {
  const $el =
    typeof modal === 'string'
      ? document.querySelector<HTMLElement>(modal.startsWith('#') ? modal : '#' + modal)
      : modal

  if ($el) {
    const card = $el.querySelector<HTMLElement>('.modal-card')!

    const animations = [
      'animate__backOutDown',
      'animate__backOutLeft',
      'animate__backOutRight',
      'animate__backOutUp',
      'animate__bounceOutUp',
      'animate__bounceOutDown',
      'animate__bounceOutLeft',
      'animate__bounceOutRight',
      'animate__fadeOutDown',
      'animate__zoomOut',
      'animate__zoomOutDown',
      'animate__zoomOutLeft',
      'animate__zoomOutRight',
      'animate__zoomOutUp',
    ]

    anim.otherAnimationByName(card, animations.pickRandom()!, false).addEventOnCompletion(() => {
      anim.otherAnimationByName($el, 'fadeOut', false, 200).addEventOnCompletion(() => {
        $el.classList.remove('is-active')
        if (onComplete) onComplete()
      })
    })
  }
}

export function closeAllModals() {
  ;(document.querySelectorAll<HTMLElement>('.modal') || []).forEach(($modal) => {
    closeModal($modal)
  })
}
