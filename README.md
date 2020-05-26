This repository contains a few minimal reproducers for a bug where the Compose runtime throws an
exception when calling `ViewGroup.setContent` to update a Composition as a side effect of clicking
a `Button` in that composition.

The crash seems to be triggered by the `onClick` handler of a `Button` inside a `setContent` call
capturing parameters or variables from the method that calls `setContent` and triggering a
trampolined `setContent` call. It does _not_ occur if the lambda does not capture.

There are four similar cases presented in this repo. All consist of a `FrameLayout` that calls
`ViewGroup.setContent` to set a composition that contains a single `Button` which displays a counter
value, and when clicked updates the counter and calls `setContent` again.

<dl>
<dt>CrashyView</dt>
<dd>
This is the most minimal reproducer I could come up with. The view initializes itself by calling
`update` with a zero counter, and when the button is clicked, it calls `update` again with an
incremented counter value. Note that the counter argument is captured by this lambda in order to
add to it.
</dd>
<dt>HappyView</dt>
<dd>
This is a copy of `CrashyView` where `update` takes no parameters, and instead the counter value is
stored as a property in the `FrameLayout`. The `onClick` lambda does not capture, and there's no
crash.
</dd>
<dt>IndirectCrashyView: crashyUpdate</dt>
<dd>
This is a more indirect version of `CrashyView`, with a simple presenter. The `update` method takes
a `ViewModel` that contains a function. The button's click handler is passed a lambda that calls
the click handler from the view model. The lambda captures the `ViewModel` argument, and it crashes.
</dd>
<dt>IndirectCrashyView: happyUpdate</dt>
<dd>
This is an exact copy of `crashyUpdate`, but instead of creating an extra lambda just to call the
`ViewModel`'s click handler, it passes the handler directly. There's no lambda, and there's no
capture, so there's no crash.
</dd>
</dl>
