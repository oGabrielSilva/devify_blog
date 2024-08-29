import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configurePseudonym(handler: InternalProfilePageHandler) {
  if (handler.profile.pseudonym.input) {
    updatePseudonymState(handler)

    handler.profile.pseudonym.input.addEventListener('input', () => updatePseudonymState(handler))
  }
}

function updatePseudonymState(handler: InternalProfilePageHandler) {
  handler.profile.pseudonym.len.text(25 - handler.profile.pseudonym.input.value.length)
}
