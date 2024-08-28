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
}
