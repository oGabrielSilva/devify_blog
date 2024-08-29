import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureBio(handler: InternalProfilePageHandler) {
  if (handler.profile.bio) {
    updateBioState(handler)

    handler.profile.bio.input.addEventListener('input', () => {
      updateBioState(handler)
    })
  }
}

export function updateBioState(handler: InternalProfilePageHandler) {
  const input = handler.profile.bio.input
  const value = 500 - input.value.length
  const helper = handler.profile.bio.len
  helper.text(value)

  if (value < 0) {
    $(input).addClass('is-danger')
    $(helper).addClass('is-danger')
    handler.profile.bio.isValid = false
  } else {
    $(input).removeClass('is-danger')
    $(helper).removeClass('is-danger')
    handler.profile.bio.isValid = true
  }
}
