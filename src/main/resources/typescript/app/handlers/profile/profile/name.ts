import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureName(handler: InternalProfilePageHandler) {
  if (handler.profile.name.input) {
    updateNameState(handler)

    handler.profile.name.input.addEventListener('input', () => updateNameState(handler))
  }
}

export function updateNameState(handler: InternalProfilePageHandler) {
  handler.profile.name.len.text(50 - handler.profile.name.input.value.length)
  if (handler.profile.name.input.value.length < 2) {
    $(handler.profile.name.input).addClass('is-danger')
    handler.profile.name.isValid = false
    handler.profile.name.helper.removeClass('is-hidden')
  } else {
    $(handler.profile.name.input).removeClass('is-danger')
    handler.profile.name.isValid = true
    handler.profile.name.helper.addClass('is-hidden')
  }
}
