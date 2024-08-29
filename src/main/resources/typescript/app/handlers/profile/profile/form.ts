import { toaster } from '../../../lib/kassiopeia-tools'
import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureProfileForm(handler: InternalProfilePageHandler) {
  handler.profile.form.on('submit', (e) => {
    e.preventDefault()

    if (!handler.profile.name.isValid) {
      toaster.danger('Nome não aceito')
      toaster.animationTool.shakeX(handler.profile.name.input)
      return
    }

    const form = handler.profile.form.get(0) as HTMLFormElement

    locker.lock()
    form?.submit()
  })
}
