'use strict';

angular.module('buenOjoApp')
    .factory('PhotoLocationExerciseMock', function($resource, DateUtils) {

        return {
            'get': function(object) {
                return {
                    id: object.id,
                    totalScore: 4,
                    lowerLevel: 5,
                    higherLevel:20,
                    
                    difficulty: "Beginner",
                    landscapeKeywords: [{
                      id: 1000,
                      name: "Colinas",
                      description: ""
                    },
                    {
                      id: 1001,
                      name: "Veg Media",
                      description: "Vegetación Media"
                    }],
                    terrainSlides: [{
                        "id": 1693,
                        "name": "DSC01709.JPG",
                        "loResImageContentType": null,
                        "hiResImage": null,
                        "hiResImageContentType": "image/jpeg",
                        "hiResImagePath": "game-resources/imageResources/DSC01709_hi_res.jpg",
                        "loResImagePath": null
                    }, {
                        "id": 1694,
                        "name": "DSC01278.JPG",
                        "loResImageContentType": null,
                        "hiResImage": null,
                        "hiResImageContentType": "image/jpeg",
                        "hiResImagePath": "game-resources/imageResources/DSC01278_hi_res.jpg",
                        "loResImagePath": null
                    }, {
                        "id": 1695,
                        "name": "DSC01212.JPG",
                        "loResImageContentType": null,
                        "hiResImage": null,
                        "hiResImageContentType": "image/jpeg",
                        "hiResImagePath": "game-resources/imageResources/DSC01212_hi_res.jpg",
                        "loResImagePath": null
                    }, {
                        "id": 1688,
                        "name": "REGLIMITE1 N.JPG",
                        "loResImageContentType": null,
                        "hiResImage": null,
                        "hiResImageContentType": "image/jpeg",
                        "hiResImagePath": "game-resources/imageResources/REGLIMITE1 N_hi_res.jpg",
                        "loResImagePath": null
                    }],
                    satelliteSlides: [{
                        "id": 1685,
                        "meters": 3217.33333333349,
                        "lon": -71.8664921848611,
                        "lat": -46.8496233187549,
                        "resolution": 3.14192708333348,
                        "name": "esriTopo_REGLIMITE1 N",
                        "image": {
                            "id": 1684,
                            "name": "esriTopo_REGLIMITE1 N.png",
                            "loResImageContentType": null,
                            "hiResImage": null,
                            "hiResImageContentType": "image/png",
                            "hiResImagePath": "game-resources/imageResources/esriTopo_REGLIMITE1 N_hi_res.png",
                            "loResImagePath": null
                        },
                        "copyright": "(c) 2015 Esri, DeLorme, FAO, USGS, NOAA.",
                        "imageType": "EsriTopo"
                    }, {
                        "id": 1683,
                        "meters": 3217.33333333349,
                        "lon": -71.8664921848611,
                        "lat": -46.8496233187549,
                        "resolution": 3.14192708333348,
                        "name": "bing_REGLIMITE1 N",
                        "image": {
                            "id": 1682,
                            "name": "bing_REGLIMITE1 N.png",
                            "loResImageContentType": null,
                            "hiResImage": null,
                            "hiResImageContentType": "image/png",
                            "hiResImagePath": "game-resources/imageResources/bing_REGLIMITE1 N_hi_res.png",
                            "loResImagePath": null
                        },
                        "copyright": "(c) 2015 Bing, Microsoft Corporation, Digital Globe, HERE.",
                        "imageType": "Bing"
                    }],
                    sightPairs: [{
                        id: 1000,
                        number: 1,
                        satelliteX: 387,
                        satelliteY: 754,
                        satelliteTolerance: 15,
                        terrainX: 235,
                        terrainY: 171,
                        terrainTolerance: 20,
                    }, {
                        id: 1001,
                        number: 2,
                        satelliteX: 513,
                        satelliteY: 433,
                        satelliteTolerance: 20,
                        terrainX: 260,
                        terrainY: 139,
                        terrainTolerance: 20,
                    }, {
                        id: 1002,
                        number: 3,
                        satelliteX: 352,
                        satelliteY: 355,
                        satelliteTolerance: 20,
                        terrainX: 207,
                        terrainY: 345,
                        terrainTolerance: 25,
                    }],
                    beacon: {
                      x: 290,
                      y: 949,
                      tolerance: 10
                    },
                    terrainPhoto: {
                        "id": 1688,
                        "name": "REGLIMITE1 N.JPG",
                        "loResImageContentType": null,
                        "hiResImage": null,
                        "hiResImageContentType": "image/jpeg",
                        "hiResImagePath": "game-resources/imageResources/REGLIMITE1 N_hi_res.jpg",
                        "loResImagePath": null
                    },
                    satelliteImages: [{
                        "id": 1685,
                        "meters": 3217.33333333349,
                        "lon": -71.8664921848611,
                        "lat": -46.8496233187549,
                        "resolution": 3.14192708333348,
                        "name": "esriTopo_REGLIMITE1 N",
                        "image": {
                            "id": 1684,
                            "name": "esriTopo_REGLIMITE1 N.png",
                            "loResImageContentType": null,
                            "hiResImage": null,
                            "hiResImageContentType": "image/png",
                            "hiResImagePath": "game-resources/imageResources/esriTopo_REGLIMITE1 N_hi_res.png",
                            "loResImagePath": null
                        },
                        "copyright": "(c) 2015 Esri, DeLorme, FAO, USGS, NOAA.",
                        "imageType": "EsriTopo"
                    }, {
                        "id": 1683,
                        "meters": 3217.33333333349,
                        "lon": -71.8664921848611,
                        "lat": -46.8496233187549,
                        "resolution": 3.14192708333348,
                        "name": "bing_REGLIMITE1 N",
                        "image": {
                            "id": 1682,
                            "name": "bing_REGLIMITE1 N.png",
                            "loResImageContentType": null,
                            "hiResImage": null,
                            "hiResImageContentType": "image/png",
                            "hiResImagePath": "game-resources/imageResources/bing_REGLIMITE1 N_hi_res.png",
                            "loResImagePath": null
                        },
                        "copyright": "(c) 2015 Bing, Microsoft Corporation, Digital Globe, HERE.",
                        "imageType": "Bing"
                    }]





                };
            }
        }
    });
