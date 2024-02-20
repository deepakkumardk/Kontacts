import 'react-native-gesture-handler';
import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {DrawerStack} from 'src/navigation';

function App() {
  return (
    <NavigationContainer>
      <DrawerStack />
    </NavigationContainer>
  );
}

export default App;
