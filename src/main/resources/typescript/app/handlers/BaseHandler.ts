export class BaseHandler {
  private static instance: BaseHandler

  public handle() {
    throw new Error('handle method not implemented')
  }

  public static get fast() {
    if (!this.instance) {
      this.instance = new this()
    }
    return this.instance
  }
}
