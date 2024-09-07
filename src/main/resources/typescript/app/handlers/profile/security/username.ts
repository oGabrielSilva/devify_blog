import { ValidationKassiopeiaTool } from 'kassiopeia-tools'
import { openModal } from '../../../lib/BulmaCss'
import { dangerElement } from '../../../utils/dangerElement'
import { InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureSecurityUsernamePart(handler: InternalProfilePageHandler) {
  if (handler.security.username.input) {
    updateUsernameState(handler)
    handler.security.username.lab.on('click', () => openModal(handler.security.username.modal[0]))

    handler.security.username.input.addEventListener('input', () => {
      handler.security.username.input.value = ValidationKassiopeiaTool.fast.slugify(
        handler.security.username.input.value
      )

      updateUsernameState(handler)
    })

    handler.security.username.form.on('submit', (e) => {
      e.preventDefault()
      if (!handler.security.username.isValid)
        return dangerElement(handler.security.username.input, 'Digite um nome de usuário maior')

      locker.lock()
      handler.security.username.form[0].submit()
    })
  }
}

export function updateUsernameState(handler: InternalProfilePageHandler) {
  handler.security.username.len.text(30 - handler.security.username.input.value.length)
  handler.security.username.isValid = handler.security.username.input.value.length >= 2

  if (!handler.security.username.isValid) $(handler.security.username.input).addClass('is-danger')
  else $(handler.security.username.input).removeClass('is-danger')
}
