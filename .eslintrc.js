module.exports = {
  root: true,
  extends: ['@react-native'],
  rules: {
    'react-hooks/exhaustive-deps': 'warn',
    'react/no-unstable-nested-components': [
      'warn',
      {
        allowAsProps: true,
        customValidators:
          [] /* optional array of validators used for propTypes validation */,
      },
    ],
  },
};
