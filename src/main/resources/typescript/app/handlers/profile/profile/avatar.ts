import { ImageKassiopeiaProcessingTool } from 'kassiopeia-tools'
import { type InternalProfilePageHandler } from '../InternalProfilePageHandler'

export function configureAvatar(handler: Omit<InternalProfilePageHandler, 'handle'>) {
  const { img, input } = handler.profile.avatar
  img.on('click', () => input.click())

  if (input) {
    input.addEventListener('input', async () => {
      if (input.files && input.files[0]) {
        locker.lock()
        handler.profile.avatar.blob =
          await ImageKassiopeiaProcessingTool.fast.convertFileToWebpBlobWithClipping(
            input.files[0],
            192,
            192,
            0.8
          )
        img.attr('src', URL.createObjectURL(handler.profile.avatar.blob))

        const dataTransfer = new DataTransfer()
        const name = input.files[0].name.replace(/\.(png|jpg|jpeg)$/i, '.webp')

        dataTransfer.items.add(
          new File([handler.profile.avatar.blob], name, {
            type: handler.profile.avatar.blob.type,
            lastModified: Date.now(),
          })
        )
        input.files = dataTransfer.files

        locker.unlock()
      }
    })
  }
}
