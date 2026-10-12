import 'package:flutter_test/flutter_test.dart';
import 'package:my_app/main.dart';

void main() {
  testWidgets('renders smart drying rack dashboard', (WidgetTester tester) async {
    await tester.pumpWidget(const SmartDryingRackApp());

    await tester.pump(const Duration(milliseconds: 1600));
    await tester.tap(find.text('Get Started'));
    await tester.pumpAndSettle();

    await tester.scrollUntilVisible(find.text('Scan for Devices'), 100);
    await tester.tap(find.text('Scan for Devices'));
    await tester.pumpAndSettle();

    expect(find.text('Smart Drying Rack'), findsWidgets);
    expect(find.text('Weather Forecast'), findsOneWidget);
    expect(find.text('Control & Schedule'), findsNothing);
  });
}
