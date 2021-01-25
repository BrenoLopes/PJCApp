FROM node:lts

RUN mkdir /home/app && chown node:node /home/app
RUN mkdir /home/app/node_modules && chown node:node /home/app/node_modules
WORKDIR  /home/app
USER node
COPY --chown=node:node package.json package-lock.json ./
RUN npm ci --quiet
COPY --chown=node:node . .

EXPOSE 4200
