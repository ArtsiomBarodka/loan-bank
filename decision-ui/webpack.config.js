const HtmlWebpackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const path = require("path");
const webpack = require("webpack");

module.exports = (env) => {
  const isProduction = env.mode === "production";
  const config = {
    mode: env.mode,
    entry: "./src/index.js",
    output: {
      filename: "bundle.js",
      //   publicPath: "/",
      path: path.join(__dirname, "dist"),
    },
    module: {
      rules: [
        {
          test: /\.js$/,
          exclude: /node_modules/,
          use: ["babel-loader"],
        },
        {
          test: /\.s?css$/i,
          use: [
            isProduction ? MiniCssExtractPlugin.loader : "style-loader",
            "css-loader",
            "sass-loader",
          ],
        },
        {
          test: /\.(jpg|png|jpeg|svg)$/i,
          use: [
            {
              loader: "url-loader",
              options: {
                limit: 8192,
                name: "[name].[ext]",
                outputPath: "image",
              },
            },
          ],
        },
      ],
    },
    plugins: [
      new HtmlWebpackPlugin({
        template: "./src/index.html",
      }),
      new CleanWebpackPlugin(),
      new webpack.ProgressPlugin(),
    ],
    devServer: {
      port: 3000,
      hot: true,
      proxy: {
        "/api": "http://localhost:8081",
      },
    },
  };

  if (isProduction) {
    config.plugins.push(
      new MiniCssExtractPlugin({
        filename: "[name].css",
      })
    );
  }

  return config;
};
