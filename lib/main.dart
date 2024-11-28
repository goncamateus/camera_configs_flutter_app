import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: CameraConfigScreen(),
    );
  }
}

class CameraConfigScreen extends StatefulWidget {
  @override
  _CameraConfigScreenState createState() => _CameraConfigScreenState();
}

class _CameraConfigScreenState extends State<CameraConfigScreen> {
  static const platform = MethodChannel('com.example.camera_configs/configs');
  List<String> cameraConfigs = [];
  String errorMessage = '';

  @override
  void initState() {
    super.initState();
    fetchCameraConfigs();
  }

  Future<void> fetchCameraConfigs() async {
    try {
      final List<dynamic> result =
          await platform.invokeMethod('getCameraConfigs');
      setState(() {
        cameraConfigs = result.cast<String>();
      });
    } on PlatformException catch (e) {
      setState(() {
        errorMessage = 'Failed to fetch camera configurations: ${e.message}';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Camera Configurations')),
      body: errorMessage.isNotEmpty
          ? Center(child: Text(errorMessage))
          : ListView.builder(
              itemCount: cameraConfigs.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(cameraConfigs[index]),
                );
              },
            ),
    );
  }
}
