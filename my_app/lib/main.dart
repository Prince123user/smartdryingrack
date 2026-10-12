import 'dart:async';

import 'package:flutter/material.dart';

void main() {
  runApp(const SmartDryingRackApp());
}

class SmartDryingRackApp extends StatelessWidget {
  const SmartDryingRackApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Smart Drying Rack',
      theme: ThemeData(
        useMaterial3: true,
        scaffoldBackgroundColor: const Color(0xFF031829),
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF36b5ff),
          brightness: Brightness.dark,
        ),
      ),
      home: const SmartDryingRackFlow(),
    );
  }
}

enum _AppPhase { splash, onboarding, setup, home }

class SmartDryingRackFlow extends StatefulWidget {
  const SmartDryingRackFlow({super.key});

  @override
  State<SmartDryingRackFlow> createState() => _SmartDryingRackFlowState();
}

class _SmartDryingRackFlowState extends State<SmartDryingRackFlow> {
  _AppPhase phase = _AppPhase.splash;

  @override
  void initState() {
    super.initState();
    Timer(const Duration(milliseconds: 1500), () {
      if (mounted) {
        setState(() => phase = _AppPhase.onboarding);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    switch (phase) {
      case _AppPhase.splash:
        return const SplashScreen();
      case _AppPhase.onboarding:
        return OnboardingScreen(
          onContinue: () => setState(() => phase = _AppPhase.setup),
        );
      case _AppPhase.setup:
        return DeviceSetupScreen(
          onContinue: () => setState(() => phase = _AppPhase.home),
        );
      case _AppPhase.home:
        return const SmartDryingRackHomePage();
    }
  }
}

class SplashScreen extends StatelessWidget {
  const SplashScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: DecoratedBox(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF041d31), Color(0xFF062844)],
          ),
        ),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                width: 128,
                height: 128,
                decoration: BoxDecoration(
                  color: const Color(0xFF0d3357),
                  borderRadius: BorderRadius.circular(32),
                  border: Border.all(color: const Color(0xFF66d9ff), width: 2),
                  boxShadow: [
                    BoxShadow(
                      color: const Color(0xFF39bfff).withValues(alpha: 0.35),
                      blurRadius: 26,
                    ),
                  ],
                ),
                child: const Icon(Icons.wifi_tethering_rounded, size: 58, color: Color(0xFF6ed7ff)),
              ),
              const SizedBox(height: 20),
              const Text(
                'Smart Drying Rack',
                style: TextStyle(
                  fontSize: 34,
                  fontWeight: FontWeight.w800,
                  color: Colors.white,
                  letterSpacing: 0.2,
                ),
              ),
              const SizedBox(height: 12),
              const Text(
                'Smart. Dry. Protected.',
                style: TextStyle(
                  fontSize: 16,
                  color: Color(0xFF9ad8ff),
                  fontWeight: FontWeight.w500,
                ),
              ),
              const SizedBox(height: 30),
              const SizedBox(
                width: 36,
                height: 36,
                child: CircularProgressIndicator(
                  strokeWidth: 3,
                  valueColor: AlwaysStoppedAnimation<Color>(Color(0xFF6ad7ff)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class OnboardingScreen extends StatelessWidget {
  final VoidCallback onContinue;

  const OnboardingScreen({super.key, required this.onContinue});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: DecoratedBox(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF041b2d), Color(0xFF07253b)],
          ),
        ),
        child: SafeArea(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(22),
            child: ConstrainedBox(
              constraints: BoxConstraints(
                minHeight: MediaQuery.of(context).size.height - MediaQuery.of(context).padding.top - MediaQuery.of(context).padding.bottom,
              ),
              child: IntrinsicHeight(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const SizedBox(height: 16),
                    Container(
                      width: 120,
                      height: 120,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(30),
                        color: const Color(0xFF0b2b49),
                        border: Border.all(color: const Color(0xFF66d9ff), width: 2),
                      ),
                      child: const Icon(Icons.wifi_tethering_rounded, size: 60, color: Color(0xFF83ddff)),
                    ),
                    const SizedBox(height: 26),
                    const Text(
                      'WELCOME TO\nSMART DRYING RACK',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 30,
                        height: 1.15,
                        fontWeight: FontWeight.w800,
                        color: Colors.white,
                        letterSpacing: 0.3,
                      ),
                    ),
                    const SizedBox(height: 24),
                    const Text(
                      'Smart drying. Protected automatically.',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 16,
                        color: Color(0xFFbfe8ff),
                      ),
                    ),
                    const SizedBox(height: 18),
                    const _CheckRow(label: 'Automatic rain protection'),
                    const _CheckRow(label: 'Smart weather monitoring'),
                    const _CheckRow(label: 'Intelligent drying schedules'),
                    const Spacer(),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: onContinue,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF4bbfff),
                          padding: const EdgeInsets.symmetric(vertical: 18),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18),
                          ),
                        ),
                        child: const Text(
                          'Get Started',
                          style: TextStyle(
                            color: Color(0xFF041b2f),
                            fontWeight: FontWeight.w800,
                            fontSize: 18,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(height: 12),
                    TextButton(
                      onPressed: onContinue,
                      child: const Text(
                        'I already have a device',
                        style: TextStyle(color: Color(0xFFb8e7ff)),
                      ),
                    )
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _CheckRow extends StatelessWidget {
  final String label;

  const _CheckRow({required this.label});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Row(
        children: [
          const Icon(Icons.check_circle_rounded, color: Color(0xFF52e0b1), size: 20),
          const SizedBox(width: 12),
          Text(label, style: const TextStyle(color: Colors.white, fontSize: 15)),
        ],
      ),
    );
  }
}

class DeviceSetupScreen extends StatelessWidget {
  final VoidCallback onContinue;

  const DeviceSetupScreen({super.key, required this.onContinue});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: DecoratedBox(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF031827), Color(0xFF081b2d)],
          ),
        ),
        child: SafeArea(
          child: SingleChildScrollView(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: ConstrainedBox(
              constraints: BoxConstraints(
                minHeight: MediaQuery.of(context).size.height - MediaQuery.of(context).padding.top - MediaQuery.of(context).padding.bottom,
              ),
              child: IntrinsicHeight(
                child: Column(
                  children: [
                    const SizedBox(height: 12),
                    const Row(
                      children: [
                        Icon(Icons.wifi_tethering_rounded, color: Color(0xFF78d9ff), size: 26),
                        SizedBox(width: 8),
                        Text('Device Setup', style: TextStyle(fontSize: 20, color: Colors.white, fontWeight: FontWeight.w700)),
                      ],
                    ),
                    const SizedBox(height: 26),
                    Container(
                      width: 120,
                      height: 120,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        gradient: const RadialGradient(
                          colors: [Color(0xFF61d3ff), Color(0xFF0d2e4c)],
                        ),
                        border: Border.all(color: const Color(0xFF6ad9ff), width: 2),
                      ),
                      child: const Icon(Icons.router_rounded, size: 54, color: Color(0xFFdff9ff)),
                    ),
                    const SizedBox(height: 18),
                    const Text(
                      'Connect Your Rack',
                      style: TextStyle(fontSize: 30, fontWeight: FontWeight.w800, color: Colors.white),
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      "Let's connect your Smart Drying Rack to get started.",
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 15, color: Color(0xFFbce4ff)),
                    ),
                    const SizedBox(height: 24),
                    ...[
                      _ConnectionOption(label: 'Wi‑Fi', subtitle: 'Recommended', selected: true),
                      _ConnectionOption(label: 'Bluetooth', subtitle: 'Connect directly', selected: false),
                      _ConnectionOption(label: 'Demo Mode', subtitle: 'Try the app without hardware', selected: false),
                    ],
                    const Spacer(),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: onContinue,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF4bbfff),
                          padding: const EdgeInsets.symmetric(vertical: 18),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18),
                          ),
                        ),
                        child: const Text(
                          'Scan for Devices',
                          style: TextStyle(
                            color: Color(0xFF041b2f),
                            fontWeight: FontWeight.w800,
                            fontSize: 18,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(height: 10),
                    const Text('Need help? View setup guide', style: TextStyle(color: Color(0xFFb9e4ff), fontSize: 13)),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _ConnectionOption extends StatelessWidget {
  final String label;
  final String subtitle;
  final bool selected;

  const _ConnectionOption({required this.label, required this.subtitle, required this.selected});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 16),
      decoration: BoxDecoration(
        color: const Color(0xFF0a243f),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: selected ? const Color(0xFF67d7ff) : const Color(0xFF234d7d)),
      ),
      child: Row(
        children: [
          Container(
            width: 44,
            height: 44,
            decoration: BoxDecoration(
              color: selected ? const Color(0xFF62d4ff).withValues(alpha: 0.18) : const Color(0xFF163755),
              borderRadius: BorderRadius.circular(14),
            ),
            child: Icon(
              selected ? Icons.wifi_rounded : Icons.bluetooth_rounded,
              color: const Color(0xFF7cd8ff),
            ),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(label, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w700, color: Colors.white)),
                const SizedBox(height: 2),
                Text(subtitle, style: const TextStyle(fontSize: 12, color: Color(0xFFa4c5de))),
              ],
            ),
          ),
          const Icon(Icons.chevron_right_rounded, color: Colors.white),
        ],
      ),
    );
  }
}

class SmartDryingRackHomePage extends StatefulWidget {
  const SmartDryingRackHomePage({super.key});

  @override
  State<SmartDryingRackHomePage> createState() => _SmartDryingRackHomePageState();
}

class _SmartDryingRackHomePageState extends State<SmartDryingRackHomePage> {
  int selectedIndex = 0;

  final List<Widget> pages = const [
    HomeScreen(),
    ControlScreen(),
    AlertsScreen(),
    HistoryScreen(),
    SettingsScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: DecoratedBox(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF021a2d), Color(0xFF071f38)],
          ),
        ),
        child: SafeArea(
          child: IndexedStack(
            index: selectedIndex,
            children: pages,
          ),
        ),
      ),
      bottomNavigationBar: Container(
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
        decoration: const BoxDecoration(
          color: Color(0xFF061d32),
          border: Border(top: BorderSide(color: Color(0xFF0f3355), width: 1)),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            _NavItem(icon: Icons.home_rounded, label: 'Home', selected: selectedIndex == 0, onTap: () => setState(() => selectedIndex = 0)),
            _NavItem(icon: Icons.dashboard_customize_rounded, label: 'Control', selected: selectedIndex == 1, onTap: () => setState(() => selectedIndex = 1)),
            _NavItem(icon: Icons.notifications_active_rounded, label: 'Alerts', selected: selectedIndex == 2, onTap: () => setState(() => selectedIndex = 2)),
            _NavItem(icon: Icons.history_rounded, label: 'History', selected: selectedIndex == 3, onTap: () => setState(() => selectedIndex = 3)),
            _NavItem(icon: Icons.settings_rounded, label: 'Settings', selected: selectedIndex == 4, onTap: () => setState(() => selectedIndex = 4)),
          ],
        ),
      ),
    );
  }
}

class _NavItem extends StatelessWidget {
  final IconData icon;
  final String label;
  final bool selected;
  final VoidCallback onTap;

  const _NavItem({
    required this.icon,
    required this.label,
    required this.selected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      behavior: HitTestBehavior.opaque,
      child: SizedBox(
        width: 60,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              size: 24,
              color: selected ? const Color(0xFF59d6ff) : const Color(0xFF9db2c8),
            ),
            const SizedBox(height: 5),
            Text(
              label,
              style: TextStyle(
                fontSize: 10,
                color: selected ? const Color(0xFFdfefff) : const Color(0xFF9db2c8),
                fontWeight: selected ? FontWeight.w700 : FontWeight.w500,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.fromLTRB(20, 18, 20, 24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                'Smart Drying Rack',
                style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700, color: Colors.white),
              ),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 11, vertical: 8),
                decoration: BoxDecoration(
                  color: const Color(0xFF0f3159),
                  borderRadius: BorderRadius.circular(16),
                  border: Border.all(color: const Color(0xFF2f76be)),
                ),
                child: const Row(
                  children: [
                    Icon(Icons.wifi_rounded, size: 14, color: Color(0xFF65f7d3)),
                    SizedBox(width: 6),
                    Text('Online', style: TextStyle(color: Colors.white, fontSize: 12)),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.fromLTRB(18, 18, 18, 14),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(30),
              gradient: const LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
                colors: [Color(0xFF0d2c4b), Color(0xFF071b2d)],
              ),
              border: Border.all(color: const Color(0xFF2a5d92), width: 1.2),
              boxShadow: [
                BoxShadow(
                  color: const Color(0xFF0d9df9).withValues(alpha: 0.24),
                  blurRadius: 32,
                  spreadRadius: 2,
                ),
              ],
            ),
            child: Column(
              children: [
                const Align(
                  alignment: Alignment.centerLeft,
                  child: Text(
                    'Smart. Dry. Protected.',
                    style: TextStyle(color: Color(0xFF9fd9ff), fontSize: 15),
                  ),
                ),
                const SizedBox(height: 12),
                const Center(child: RackIllustration(size: Size(260, 180))),
                const SizedBox(height: 12),
                const Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text('Rack status', style: TextStyle(color: Color(0xFFbfdaf5), fontSize: 12)),
                    Text('Safe to dry', style: TextStyle(color: Color(0xFF7ef0c4), fontSize: 12, fontWeight: FontWeight.w700)),
                  ],
                ),
                const SizedBox(height: 18),
                Row(
                  children: [
                    Expanded(child: _MetricTile(label: 'Temp', value: '31°C', color: const Color(0xFF7ed9ff))),
                    const SizedBox(width: 12),
                    Expanded(child: _MetricTile(label: 'Humidity', value: '68%', color: const Color(0xFF8ee7c2))),
                  ],
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    Expanded(child: _MetricTile(label: 'Rain', value: 'Dry', color: const Color(0xFFE8F6FF))),
                    const SizedBox(width: 12),
                    Expanded(child: _MetricTile(label: 'Drying', value: '62%', color: const Color(0xFF95d6ff))),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(height: 18),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: const Color(0xFF0a243f),
              borderRadius: BorderRadius.circular(24),
              border: Border.all(color: const Color(0xFF224d7f)),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text('Weather Forecast', style: TextStyle(fontSize: 18, fontWeight: FontWeight.w700, color: Colors.white)),
                const SizedBox(height: 12),
                Row(
                  children: [
                    const Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Partly Cloudy', style: TextStyle(fontSize: 12, color: Color(0xFFb8d6f5))),
                        SizedBox(height: 6),
                        Text('Rain by 4:00 PM', style: TextStyle(fontSize: 12, color: Color(0xFF7fd7ff))),
                      ],
                    ),
                    const Spacer(),
                    RichText(
                      text: const TextSpan(
                        children: [
                          TextSpan(text: '31', style: TextStyle(fontSize: 38, fontWeight: FontWeight.w700, color: Colors.white)),
                          TextSpan(text: '°', style: TextStyle(fontSize: 24, fontWeight: FontWeight.w700, color: Colors.white)),
                        ],
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: const LinearProgressIndicator(
                    value: 0.62,
                    minHeight: 10,
                    backgroundColor: Color(0xFF163d69),
                    valueColor: AlwaysStoppedAnimation<Color>(Color(0xFF7edbff)),
                  ),
                ),
                const SizedBox(height: 10),
                const Text('Drying progress', style: TextStyle(color: Color(0xFFbfdaf5), fontSize: 12)),
              ],
            ),
          ),
          const SizedBox(height: 18),
          Row(
            children: [
              Expanded(child: _ActionCard(icon: Icons.sensors_rounded, title: 'Smart Insight', subtitle: 'Good conditions for drying', color: const Color(0xFF41d7b1))),
              const SizedBox(width: 12),
              Expanded(child: _ActionCard(icon: Icons.warning_amber_rounded, title: 'Alerts', subtitle: 'Rain warning active', color: const Color(0xFFf5b25e))),
            ],
          ),
        ],
      ),
    );
  }
}

class ControlScreen extends StatelessWidget {
  const ControlScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 18, 20, 24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('Control & Schedule', style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700, color: Colors.white)),
          const SizedBox(height: 18),
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: const Color(0xFF0a243f),
              borderRadius: BorderRadius.circular(24),
              border: Border.all(color: const Color(0xFF264f7d)),
            ),
            child: Column(
              children: [
                Row(
                  children: [
                    Expanded(child: _SegmentButton(label: 'Auto', selected: true)),
                    const SizedBox(width: 12),
                    Expanded(child: _SegmentButton(label: 'Manual', selected: false)),
                  ],
                ),
                const SizedBox(height: 18),
                Row(
                  children: [
                    Expanded(child: _ControlButton(icon: Icons.keyboard_double_arrow_up_rounded, label: 'Extend')),
                    const SizedBox(width: 12),
                    Expanded(child: _ControlButton(icon: Icons.stop_circle_rounded, label: 'Stop')),
                    const SizedBox(width: 12),
                    Expanded(child: _ControlButton(icon: Icons.keyboard_double_arrow_down_rounded, label: 'Retract')),
                  ],
                ),
                const SizedBox(height: 18),
                Container(
                  padding: const EdgeInsets.all(14),
                  decoration: BoxDecoration(
                    color: const Color(0xFF0f2f52),
                    borderRadius: BorderRadius.circular(18),
                  ),
                  child: const Row(
                    children: [
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text('Schedule', style: TextStyle(color: Color(0xFFb9dbff), fontSize: 13)),
                            SizedBox(height: 8),
                            Text('Rack Out', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.w600)),
                          ],
                        ),
                      ),
                      Text('8:00 AM', style: TextStyle(color: Color(0xFF8ad7ff), fontSize: 15, fontWeight: FontWeight.w700)),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class AlertsScreen extends StatelessWidget {
  const AlertsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final alerts = [
      ('Rain Detected', '2:14 PM', true),
      ('Clothes Dry', '11:40 AM', false),
      ('Forecast Warning', '1:30 PM', true),
      ('Device Disconnected', '8:05 AM', true),
    ];

    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 18, 20, 24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('Alerts', style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700, color: Colors.white)),
          const SizedBox(height: 18),
          ...alerts.map((alert) {
            final color = alert.$3 ? const Color(0xFFffb357) : const Color(0xFF74e0b4);
            return Container(
              margin: const EdgeInsets.only(bottom: 12),
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: const Color(0xFF0a243f),
                borderRadius: BorderRadius.circular(18),
                border: Border.all(color: const Color(0xFF234d7e)),
              ),
              child: Row(
                children: [
                  Container(
                    width: 36,
                    height: 36,
                    decoration: BoxDecoration(color: color.withValues(alpha: 0.18), borderRadius: BorderRadius.circular(12)),
                    child: Icon(alert.$3 ? Icons.warning_amber_rounded : Icons.check_circle_rounded, color: color),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(alert.$1, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w700)),
                        const SizedBox(height: 4),
                        Text(alert.$2, style: const TextStyle(color: Color(0xFFa9c5df), fontSize: 12)),
                      ],
                    ),
                  ),
                  Icon(Icons.chevron_right_rounded, color: const Color(0xFFbdd9f5)),
                ],
              ),
            );
          }),
        ],
      ),
    );
  }
}

class HistoryScreen extends StatelessWidget {
  const HistoryScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 18, 20, 24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('History', style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700, color: Colors.white)),
          const SizedBox(height: 18),
          Row(
            children: [
              Expanded(child: _SummaryCard(label: 'Drying Time', value: '3h 20m', color: const Color(0xFF69d9ff))),
              const SizedBox(width: 12),
              Expanded(child: _SummaryCard(label: 'Rain Saves', value: '4', color: const Color(0xFF8fe7c6))),
            ],
          ),
          const SizedBox(height: 18),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(18),
            decoration: BoxDecoration(
              color: const Color(0xFF0a243f),
              borderRadius: BorderRadius.circular(24),
              border: Border.all(color: const Color(0xFF274d79)),
            ),
            child: const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Recent Events', style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.w700)),
                SizedBox(height: 12),
                _HistoryLine(icon: Icons.cloud_queue_rounded, text: 'Rain retracted (rain) 2:14 PM'),
                _HistoryLine(icon: Icons.check_circle_rounded, text: 'Clothes dry alert 11:40 AM'),
                _HistoryLine(icon: Icons.expand_rounded, text: 'Rack extended 8:00 AM'),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = [
      ('Smart Drying Rack', true),
      ('Rain Protection', true),
      ('Weather Protection', true),
      ('Drying Threshold', false),
      ('Device Settings', false),
    ];

    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 18, 20, 24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('Settings', style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700, color: Colors.white)),
          const SizedBox(height: 18),
          ...settings.map((setting) {
            final isEnabled = setting.$2;
            return Container(
              margin: const EdgeInsets.only(bottom: 12),
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
              decoration: BoxDecoration(
                color: const Color(0xFF0a243f),
                borderRadius: BorderRadius.circular(18),
                border: Border.all(color: const Color(0xFF224d7f)),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      setting.$1,
                      style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.w500),
                    ),
                  ),
                  Switch(
                    value: isEnabled,
                    activeThumbColor: const Color(0xFF63d8ff),
                    activeTrackColor: const Color(0xFF63d8ff).withValues(alpha: 0.35),
                    onChanged: (_) {},
                  ),
                ],
              ),
            );
          }),
        ],
      ),
    );
  }
}

class _MetricTile extends StatelessWidget {
  final String label;
  final String value;
  final Color color;

  const _MetricTile({required this.label, required this.value, required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: const Color(0xFF0d2c4b),
        borderRadius: BorderRadius.circular(18),
        border: Border.all(color: const Color(0xFF2c5c92)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: const TextStyle(color: Color(0xFFc6dffb), fontSize: 12)),
          const SizedBox(height: 6),
          Text(value, style: TextStyle(color: color, fontSize: 28, fontWeight: FontWeight.w700)),
        ],
      ),
    );
  }
}

class _ActionCard extends StatelessWidget {
  final IconData icon;
  final String title;
  final String subtitle;
  final Color color;

  const _ActionCard({
    required this.icon,
    required this.title,
    required this.subtitle,
    required this.color,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF0a243f),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFF214d7d)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: 42,
            height: 42,
            decoration: BoxDecoration(color: color.withValues(alpha: 0.18), borderRadius: BorderRadius.circular(12)),
            child: Icon(icon, color: color),
          ),
          const SizedBox(height: 12),
          Text(title, style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.w700)),
          const SizedBox(height: 6),
          Text(subtitle, style: const TextStyle(color: Color(0xFFb2d1ee), fontSize: 12)),
        ],
      ),
    );
  }
}

class _SegmentButton extends StatelessWidget {
  final String label;
  final bool selected;

  const _SegmentButton({required this.label, required this.selected});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 12),
      decoration: BoxDecoration(
        color: selected ? const Color(0xFF62d2ff) : const Color(0xFF0f3054),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Center(
        child: Text(
          label,
          style: TextStyle(
            color: selected ? const Color(0xFF041b30) : const Color(0xFFd9ebff),
            fontWeight: FontWeight.w700,
          ),
        ),
      ),
    );
  }
}

class _ControlButton extends StatelessWidget {
  final IconData icon;
  final String label;

  const _ControlButton({required this.icon, required this.label});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 18),
      decoration: BoxDecoration(
        color: const Color(0xFF0f2f52),
        borderRadius: BorderRadius.circular(18),
        border: Border.all(color: const Color(0xFF214d7d)),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, color: const Color(0xFF67d8ff), size: 28),
          const SizedBox(height: 8),
          Text(label, style: const TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.w600)),
        ],
      ),
    );
  }
}

class _SummaryCard extends StatelessWidget {
  final String label;
  final String value;
  final Color color;

  const _SummaryCard({required this.label, required this.value, required this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF0a243f),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFF214d7d)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: const TextStyle(color: Color(0xFFb3d0ee), fontSize: 12)),
          const SizedBox(height: 8),
          Text(value, style: TextStyle(color: color, fontSize: 28, fontWeight: FontWeight.w700)),
        ],
      ),
    );
  }
}

class _HistoryLine extends StatelessWidget {
  final IconData icon;
  final String text;

  const _HistoryLine({required this.icon, required this.text});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Row(
        children: [
          Icon(icon, color: const Color(0xFF76d8ff), size: 18),
          const SizedBox(width: 12),
          Expanded(child: Text(text, style: const TextStyle(color: Color(0xFFD9EEFF), fontSize: 14))),
        ],
      ),
    );
  }
}

class RackIllustration extends StatelessWidget {
  const RackIllustration({super.key, required this.size});

  final Size size;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: size.width,
      height: size.height,
      child: CustomPaint(painter: RackPainter()),
    );
  }
}

class RackPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final bg = Paint()..color = const Color(0xFF071f38);
    canvas.drawRect(Offset.zero & size, bg);

    final sky = Paint()..color = const Color(0xFF4ac1ff).withValues(alpha: 0.25);
    final cloud = Paint()..color = const Color(0xFFbfefff).withValues(alpha: 0.4);

    canvas.drawCircle(Offset(size.width * 0.2, size.height * 0.2), 18, sky);
    canvas.drawCircle(Offset(size.width * 0.28, size.height * 0.17), 22, sky);
    canvas.drawCircle(Offset(size.width * 0.36, size.height * 0.2), 16, sky);
    canvas.drawOval(Rect.fromLTWH(size.width * 0.08, size.height * 0.12, size.width * 0.28, size.height * 0.12), cloud);

    final rackPaint = Paint()
      ..color = const Color(0xFF6dc8ff)
      ..strokeWidth = 5
      ..style = PaintingStyle.stroke;

    final framePaint = Paint()
      ..color = const Color(0xFFaee8ff)
      ..strokeWidth = 3
      ..style = PaintingStyle.stroke;

    final groundPaint = Paint()
      ..color = const Color(0xFF0c5b7a)
      ..style = PaintingStyle.fill;

    final linePaint = Paint()
      ..color = const Color(0xFF4dd1ff)
      ..strokeWidth = 2;

    final railLeft = Offset(size.width * 0.12, size.height * 0.2);
    final railRight = Offset(size.width * 0.88, size.height * 0.2);
    final railBottom = Offset(size.width * 0.5, size.height * 0.8);

    canvas.drawLine(railLeft, railBottom, rackPaint);
    canvas.drawLine(railRight, railBottom, rackPaint);
    canvas.drawLine(Offset(size.width * 0.15, size.height * 0.3), Offset(size.width * 0.85, size.height * 0.3), framePaint);
    canvas.drawLine(Offset(size.width * 0.15, size.height * 0.5), Offset(size.width * 0.85, size.height * 0.5), framePaint);

    for (int i = 0; i < 5; i++) {
      final x = size.width * (0.2 + i * 0.15);
      canvas.drawLine(Offset(x, size.height * 0.18), Offset(x, size.height * 0.7), linePaint);
    }

    for (int i = 0; i < 4; i++) {
      final y = size.height * (0.25 + i * 0.12);
      canvas.drawLine(Offset(size.width * 0.12, y), Offset(size.width * 0.88, y), linePaint);
    }

    final clothPaint = Paint()
      ..color = const Color(0xFFebf9ff)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 3;

    for (int i = 0; i < 4; i++) {
      final left = Offset(size.width * (0.18 + i * 0.14), size.height * 0.18);
      final right = Offset(size.width * (0.28 + i * 0.14), size.height * 0.18);
      canvas.drawLine(left, Offset((left.dx + right.dx) / 2, size.height * 0.62), clothPaint);
      canvas.drawLine(right, Offset((left.dx + right.dx) / 2, size.height * 0.62), clothPaint);
      canvas.drawLine(Offset((left.dx + right.dx) / 2, size.height * 0.62), Offset((left.dx + right.dx) / 2, size.height * 0.72), clothPaint);
    }

    final groundRect = Rect.fromLTWH(0, size.height * 0.72, size.width, size.height * 0.28);
    canvas.drawRect(groundRect, groundPaint);
    canvas.drawLine(Offset(0, size.height * 0.72), Offset(size.width, size.height * 0.72), Paint()..color = const Color(0xFF6deaff)..strokeWidth = 2);
  }

  @override
  bool shouldRepaint(covariant RackPainter oldDelegate) => false;
}
