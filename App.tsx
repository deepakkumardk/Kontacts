import 'react-native-gesture-handler';
import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {MD3LightTheme, PaperProvider} from 'react-native-paper';

import {RootNavigator} from 'src/navigation/RootNavigator';

const theme = {
  ...MD3LightTheme,
  colors: {
    ...MD3LightTheme.colors,
  },
};

function App() {
  return (
    <NavigationContainer>
      <PaperProvider theme={theme}>
        <RootNavigator />
      </PaperProvider>
    </NavigationContainer>
  );
}

export default App;
