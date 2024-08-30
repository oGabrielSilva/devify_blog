import { ValidationKassiopeiaTool } from 'kassiopeia-tools'
import { openModal } from '../../../lib/BulmaCss'
import { toaster } from '../../../lib/kassiopeia-tools'
import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureSecurityPartEmail(handler: InternalProfilePageHandler) {
  const part = handler.security

  part.email.lab.on('click', () => openModal(part.email.modal.get(0)!))

  part.email.input.on('input', () => updateEmailState(handler))
  part.email.password.input.on('input', () => updatePasswordState(handler))
  part.email.form.on('submit', (e) => {
    e.preventDefault()
    updateEmailState(handler)
    updatePasswordState(handler)

    if (!part.email.isValid) {
      toaster.danger('Informe um endereço de email válido')
      anim.shakeX(part.email.input.get(0)!)
      return
    }

    if (!part.email.password.isValid) {
      toaster.danger('Informe uma senha válida')
      anim.shakeX(part.email.password.input.get(0)!)
      anim.shakeX(part.email.password.helper.get(0)!)
      return
    }

    e.currentTarget.submit()
  })
}

export function updateEmailState(handler: InternalProfilePageHandler) {
  const email = handler.security.email.input

  if (ValidationKassiopeiaTool.fast.isEmailValid(email.val()!)) {
    email.removeClass('is-danger')
    handler.security.email.isValid = true
  } else {
    email.addClass('is-danger')
    handler.security.email.isValid = false
  }
}

export function updatePasswordState(handler: InternalProfilePageHandler) {
  const password = handler.security.email.password

  if (ValidationKassiopeiaTool.fast.isPasswordValid(password.input.val() as string)) {
    password.input.removeClass('is-danger')
    password.helper.addClass('is-hidden')
    handler.security.email.password.isValid = true
  } else {
    password.input.addClass('is-danger')
    handler.security.email.password.isValid = false
    password.helper.removeClass('is-hidden')
  }
}
