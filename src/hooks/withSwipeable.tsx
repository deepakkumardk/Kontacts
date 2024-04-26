import React from 'react';
import {Swipeable} from 'react-native-gesture-handler';

export const withSwipeable = (WrapperComponent: any) => {
  const InnerComponent = (props: any) => {
    return (
      <Swipeable>
        <WrapperComponent {...props} />
      </Swipeable>
    );
  };

  return InnerComponent;
};
