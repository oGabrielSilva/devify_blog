import { ValidationKassiopeiaTool } from 'kassiopeia-tools'
import { toaster } from '../../lib/kassiopeia-tools'
import { BaseHandler } from '../BaseHandler'

export class SessionPageHandler extends BaseHandler {
  private readonly form = document.querySelector('form') as HTMLFormElement
  private readonly email = this.form.querySelector('#email') as HTMLElement
  private isEmailValid = false
  private readonly password = this.form.querySelector('#password') as HTMLElement
  private isPasswordValid = false
  private readonly signUpButton = this.form.querySelector('#sign-up') as HTMLButtonElement

  private readonly params = new URLSearchParams(globalThis.location.search)
  private isForbidden = false

  private configureEmail() {
    const input = $(this.email).find('input')
    const helper = $(this.email).find('.help')

    input.on('input', (e) => {
      this.isEmailValid = ValidationKassiopeiaTool.fast.isEmailValid(e.currentTarget.value)

      if (this.isEmailValid) {
        helper.addClass('is-hidden')
        input.removeClass('is-danger')
        return
      }
      helper.removeClass('is-hidden')
      input.addClass('is-danger')
    })
  }

  private configurePassword() {
    const input = $(this.password).find('input')
    const helper = $(this.password).find('.help')

    input.on('input', (e) => {
      this.isPasswordValid = ValidationKassiopeiaTool.fast.isPasswordValid(e.currentTarget.value)

      if (this.isPasswordValid) {
        helper.addClass('is-hidden')
        input.removeClass('is-danger')
        return
      }
      helper.removeClass('is-hidden')
      input.addClass('is-danger')
    })
  }

  private configureSignUp() {
    $(this.signUpButton).on('click', () => {
      if (this.isFormValid()) {
        this.form.action = this.signUpButton.dataset.action as string
        this.form.querySelector<HTMLButtonElement>('button[type="submit"]')?.click()
      }
    })
  }

  private isFormValid() {
    this.isEmailValid = ValidationKassiopeiaTool.fast.isEmailValid($(this.email).find('input').get(0)!.value)
    this.isPasswordValid = ValidationKassiopeiaTool.fast.isPasswordValid(
      $(this.password).find('input').get(0)!.value
    )

    if (!this.isEmailValid) {
      toaster.danger('Email não é válido')
      toaster.animationTool.shakeX(this.email)
      return false
    }

    if (!this.isPasswordValid) {
      toaster.danger('Senha não é válida')
      toaster.animationTool.shakeX(this.password)
      return false
    }

    return this.isEmailValid && this.isPasswordValid
  }

  private configureForm() {
    this.configureEmail()
    this.configurePassword()
    this.configureSignUp()

    $(this.form).on('submit', (e) => {
      e.preventDefault()
      if (this.isFormValid()) {
        // if (!this.isForbidden) return this.form.submit()
        //Implementar login por json
        this.form.submit()
      }
    })
  }

  public handle() {
    this.configureForm()

    if (this.params.get('forbidden') === 'true') {
      toaster.danger('Sua sessão expirou. Por favor, faça login novamente para continuar', 12000)
      this.isForbidden = true
    }
  }
}
