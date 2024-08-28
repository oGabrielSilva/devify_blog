export function configureDirectionButtons() {
  const directionButtons = document.querySelector('#direction-buttons')
  if (directionButtons) {
    $(directionButtons)
      .find<HTMLButtonElement>('button')
      .each((_, button) => {
        if (button.id === 'direction-up') {
          $(button).on('click', () => {
            self.scrollTo({ behavior: 'smooth', left: 0, top: 0 })
          })
          return
        }
        if (button.id === 'direction-middle') {
          $(button).on('click', () => {
            self.scrollTo({
              behavior: 'smooth',
              left: 0,
              top: (document.body.clientHeight - innerHeight) / 2,
            })
          })
          return
        }
        if (button.id === 'direction-down') {
          $(button).on('click', () => {
            self.scrollTo({
              behavior: 'smooth',
              left: 0,
              top: document.body.clientHeight,
            })
          })
        }
      })
  }
}
