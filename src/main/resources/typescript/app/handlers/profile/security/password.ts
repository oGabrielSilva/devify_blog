import { ValidationKassiopeiaTool } from 'kassiopeia-tools'
import { openModal } from '../../../lib/BulmaCss'
import { dangerElement } from '../../../utils/dangerElement'
import { InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureChangePasswordPart(handler: InternalProfilePageHandler) {
  const part = handler.security.password

  part.button.on('click', () => openModal(part.modal.get(0)!))

  part.inputs.current.input.on('input', () => updateCurrentPasswordInputState(handler))
  part.inputs.new.input.on('input', () => updateNewPasswordInputState(handler))
  part.inputs.confirm.input.on('input', () => updateConfirmNewPasswordInputState(handler))
  part.form.on('submit', (e) => {
    e.preventDefault()
    updateCurrentPasswordInputState(handler)
    updateNewPasswordInputState(handler)
    updateConfirmNewPasswordInputState(handler)
    const data = handler.security.password.inputs

    if (!data.current.isValid) {
      return dangerElement(data.current.input.get(0)!, 'Digite uma senha válida')
    }
    if (!data.new.isValid) {
      return dangerElement(data.new.input.get(0)!, 'Nova senha precisa ser válida')
    }
    if (!data.confirm.isValid) {
      return dangerElement(data.confirm.input.get(0)!, 'As senhas não são iguais')
    }

    part.form.get(0)?.submit()
  })
}

export function updateCurrentPasswordInputState(handler: InternalProfilePageHandler) {
  handler.security.password.inputs.current.isValid = ValidationKassiopeiaTool.fast.isPasswordValid(
    handler.security.password.inputs.current.input.val() as string
  )

  if (handler.security.password.inputs.current.isValid) {
    handler.security.password.inputs.current.helper.addClass('is-hidden')
    handler.security.password.inputs.current.input.removeClass('is-danger')
  } else {
    handler.security.password.inputs.current.helper.removeClass('is-hidden')
    handler.security.password.inputs.current.input.addClass('is-danger')
  }
}

export function updateNewPasswordInputState(handler: InternalProfilePageHandler) {
  handler.security.password.inputs.new.isValid =
    ValidationKassiopeiaTool.fast.isPasswordValid(
      handler.security.password.inputs.new.input.val() as string
    ) &&
    handler.security.password.inputs.new.input.val() !== handler.security.password.inputs.current.input.val()

  if (handler.security.password.inputs.new.isValid) {
    handler.security.password.inputs.new.helper.addClass('is-hidden')
    handler.security.password.inputs.new.input.removeClass('is-danger')
  } else {
    handler.security.password.inputs.new.helper.removeClass('is-hidden')
    handler.security.password.inputs.new.input.addClass('is-danger')
  }
}

export function updateConfirmNewPasswordInputState(handler: InternalProfilePageHandler) {
  handler.security.password.inputs.confirm.isValid =
    ValidationKassiopeiaTool.fast.isPasswordValid(
      handler.security.password.inputs.confirm.input.val() as string
    ) &&
    handler.security.password.inputs.confirm.input.val() === handler.security.password.inputs.new.input.val()

  if (handler.security.password.inputs.confirm.isValid) {
    handler.security.password.inputs.confirm.input.removeClass('is-danger')
  } else {
    handler.security.password.inputs.confirm.input.addClass('is-danger')
  }
}
