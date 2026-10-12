# Design direction: Garden Home

A calm household companion with warm white cards, pale sage backgrounds and forest-green actions. The dashboard answers three questions in order: is the rack connected, where is it, and what are the sensors measuring? The design is implemented in the Android app; `design-concept.svg` illustrates the intended hierarchy rather than a captured runtime screen.

| Element | Direction |
| --- | --- |
| Background | Pale sage `#F4F8F5` |
| Main action | Forest green `#16734B` |
| Cards | White, rounded corners, quiet borders |
| Text | Dark slate, sentence case, short labels |
| Attention | Amber for rain/stale readings; red for faults/stop |
| Touch targets | At least 48 dp for primary actions |
| Layout | Two sensor columns on phones, split dashboard on tablets |
| Navigation | Home, Controls, Alerts, History; connection/settings in header |

## Implemented UX improvements

- Connection setup saves a real rack address, validates it, reports connection errors and advances only after valid telemetry. Exploring offline is clearly labelled.
- Unknown readings use an em dash. Missing forecasts and dryness sensors explain what is unavailable. No invented drying time or generic sunny-day advice.
- Green is reserved for normal operation and primary actions. Status text accompanies color. Stop remains visually distinct.
- Controls explain schedule limitations and invalid time windows. Weekday chips wrap on narrow screens.
- Settings persist, and the light/dark switch updates shared colors across screens.
- Alerts and history display saved events with real timestamps and empty states. Command wording distinguishes acceptance from completion.

## Next design ideas

1. **Drying session card:** explicitly start a laundry session and record measured dryness milestones before showing time estimates or a weekly chart.
2. **Rack-first onboarding:** QR code on the enclosure with its identity/address, followed by a status test and guided limit-switch check.
3. **Action feedback:** a single movement timeline showing “request accepted → moving → limit switch reached”, with a persistent fault recovery action.
4. **Weather planning:** connect a real provider and saved rack location, show forecast time/source/freshness, and recommend a drying window. Never mix predictions with sensor measurements.
5. **Accessible household modes:** support large text, screen readers and Filipino translations; validate on a real phone before release.
6. **Firmware autonomy:** show whether device-side rain protection and schedules are active, using firmware-reported capabilities rather than assumed badges.

## Validation limits

The cloud build, JVM regression tests and Android lint validate compilation and logic. No emulator/phone screenshot or visual interaction test has been performed. Test the layout at large font sizes, on narrow screens, with TalkBack and with actual hardware before release.
