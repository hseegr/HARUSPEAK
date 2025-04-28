// ESLint의 기본 JavaScript 규칙을 가져옵니다.
import js from '@eslint/js';
// TypeScript 지원을 위한 ESLint 플러그인 및 파서를 가져옵니다.
import typescriptEslint from '@typescript-eslint/eslint-plugin';
import typescriptParser from '@typescript-eslint/parser';
// 파일 및 폴더 명명 규칙을 검사하는 플러그인을 가져옵니다.
import checkFile from 'eslint-plugin-check-file';
// import 문 관련 규칙을 제공하는 플러그인을 가져옵니다.
import importPlugin from 'eslint-plugin-import';
// 화살표 함수 사용을 권장하는 플러그인을 가져옵니다.
import preferArrow from 'eslint-plugin-prefer-arrow';
// 코드 포매팅을 위한 Prettier 플러그인을 가져옵니다.
import prettier from 'eslint-plugin-prettier';
// React 관련 규칙을 제공하는 플러그인을 가져옵니다.
import reactPlugin from 'eslint-plugin-react';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
// 다양한 유틸리티 규칙을 제공하는 플러그인을 가져옵니다.
import unicorn from 'eslint-plugin-unicorn';
// 브라우저 및 Node.js 전역 변수를 설정합니다.
import globals from 'globals';

export default [
  {
    // ESLint가 무시할 디렉토리를 지정합니다.
    ignores: ['dist', 'node_modules'],
  },
  // ESLint의 기본 권장 설정을 적용합니다.
  js.configs.recommended,
  {
    // TypeScript 파일에 대한 설정을 지정합니다.
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      // 최신 ECMAScript 버전을 사용합니다.
      ecmaVersion: 'latest',
      // 모듈 시스템을 사용합니다.
      sourceType: 'module',
      // TypeScript 파서를 사용합니다.
      parser: typescriptParser,
      // 브라우저 및 Node.js 전역 변수를 설정합니다.
      globals: {
        ...globals.browser,
        ...globals.node,
      },
    },
    // 사용될 ESLint 플러그인을 지정합니다.
    plugins: {
      '@typescript-eslint': typescriptEslint,
      import: importPlugin,
      unicorn: unicorn,
      'check-file': checkFile,
      prettier: prettier,
      react: reactPlugin,
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      'prefer-arrow': preferArrow,
    },
    // ESLint 규칙을 설정합니다.
    rules: {
      // 누락된 import 문을 오류로 처리합니다.
      'import/no-unresolved': [
        'error',
        {
          ignore: ['^virtual:'], // 가상 모듈은 무시
        },
      ],

      // 폴더 이름은 camelCase를 사용하도록 강제합니다.
      'check-file/folder-naming-convention': ['error', { '**/': 'CAMEL_CASE' }],

      // 파일 이름은 kebab-case 또는 PascalCase를 허용합니다.
      'unicorn/filename-case': [
        'error',
        {
          cases: {
            kebabCase: true,
            pascalCase: true,
          },
        },
      ],

      // 변수, 함수, 파라미터, 속성명은 camelCase 또는 PascalCase를 허용합니다.
      '@typescript-eslint/naming-convention': [
        'error',
        {
          selector: ['variable', 'function', 'parameter', 'property'],
          format: ['camelCase', 'PascalCase'],
          leadingUnderscore: 'allow',
          trailingUnderscore: 'allow',
        },
        {
          // 속성명에 대해 '@' 기호 허용
          selector: 'property',
          format: ['camelCase', 'PascalCase'],
          leadingUnderscore: 'allow',
          trailingUnderscore: 'allow',
          // 특수 기호 '@'를 허용하는 정규식 패턴
          filter: {
            // '@'로 시작하는 속성명을 허용
            regex: '^@',
            match: true,
          },
        },
        {
          selector: 'property',
          modifiers: ['requiresQuotes'], // 따옴표 속성명 허용
          format: null, // 포맷 검사하지 않음
        },
        {
          // 클래스, 인터페이스, 타입 등은 PascalCase를 사용하도록 강제합니다.
          selector: 'typeLike',
          format: ['PascalCase'],
        },
      ],

      // JSX에서 컴포넌트 이름은 PascalCase를 사용하도록 강제합니다.
      'react/jsx-pascal-case': 'error',
      // 인라인 스타일 속성은 객체 형태로 작성하도록 강제합니다.
      'react/style-prop-object': 'error',
      // React 컴포넌트는 default export만 허용하도록 경고합니다.
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
      // React Hooks 규칙을 적용합니다.
      ...reactHooks.configs.recommended.rules,

      // Prettier 포맷팅 규칙을 경고로 처리합니다.
      'prettier/prettier': 'warn',

      // 함수 선언 대신 화살표 함수 사용을 권장합니다.
      'prefer-arrow/prefer-arrow-functions': [
        'warn',
        {
          disallowPrototype: true,
          singleReturnOnly: false,
          classPropertiesAllowed: false,
        },
      ],
    },
    // import 문 해석을 위한 설정을 지정합니다.
    settings: {
      'import/resolver': {
        typescript: {
          project: './tsconfig.json', // shadcn 설치하면서 eslint 에러발생해서 추가.
        },
      },
    },
  },
];
