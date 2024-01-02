package io.micro.core.valid

import jakarta.validation.groups.Default

interface ValidGroup {

    interface Create : Default

    interface Update : Default

}